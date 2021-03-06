package com.example.crowdzrnew.di

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.example.crowdzrnew.AppPreference
import com.example.crowdzrnew.AppResourceProvider
import com.example.crowdzrnew.BuildConfig
import com.example.crowdzrnew.core.Router
import com.example.crowdzrnew.core.util.SchedulerProvider
import com.example.crowdzrnew.database.CrowdzrDatabase
import com.example.crowdzrnew.rest.GeneralService
import com.example.crowdzrnew.rest.api.TokenService
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {

    companion object {
        // API Repository
//        private const val URL = "https://api.github.com/"
        private const val URL = "https://my-json-server.typicode.com/addam01/demoJson/"
    }

    @Provides
    @Singleton
    fun getContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun getRouter(): Router {
        return Router()
    }

    @Provides
    @Singleton
    fun provideSchedulerProvider() = SchedulerProvider(Schedulers.io(), AndroidSchedulers.mainThread())

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

    @Provides
    @Singleton
    fun provideDb(application: Application):CrowdzrDatabase{
        return Room.databaseBuilder(application,CrowdzrDatabase::class.java,"crowdzr")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserProfile(db:CrowdzrDatabase) =db.userProfileDao()


//    @Provides
//    @Singleton
//    fun provideDb(application: Application): AppDatabase {
////        val factory = SafeHelperFactory.fromUser(SpannableStringBuilder(String(Base64.decode(Constants.getDBPassphrase(), Base64.DEFAULT))))
//        return Room.databaseBuilder(application, AppDatabase::class.java, "appDatabase")
//            .fallbackToDestructiveMigration()
////            .openHelperFactory(factory)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideUserDao(db: AppDatabase) = db.userDao()

    /**
     * If your API has token and you need to refresh it with interceptor, use this
     */
//    @Provides
//    @Named("real")
//    @Singleton
//    fun provideOkHttpClient(application: Application, tokenRepository: TokenRepository,
//                            schedulerProvider: SchedulerProvider, iPayEasyPreference: IPayEasyPreference): OkHttpClient {
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
//        val tokenInterceptor = TokenRefreshInterceptor(tokenRepository, schedulerProvider, iPayEasyPreference)
//        val cacheDir = File(application.cacheDir, UUID.randomUUID().toString())
//        val cache = Cache(cacheDir, 10 * 1024 * 1024)
//
//        return OkHttpClient.Builder()
//            .cache(cache)
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(60, TimeUnit.SECONDS)
//            .writeTimeout(60, TimeUnit.SECONDS)
//            .addInterceptor { chain ->
//                Timber.d { "Added Authorization" }
//                val original = chain.request()
//                val builder = original.newBuilder()
//                builder.addHeader("Authorization", "Bearer " + iPayEasyPreference.getAccessToken())
//                val request = builder.build()
//                chain.proceed(request)
//            }
//            .addInterceptor(interceptor)
//            .addInterceptor(tokenInterceptor)
//            .build()
//    }

    @Provides
    @Named("real")
    @Singleton
    fun provideOkHttpClientCredential(application: Application): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val cacheDir = File(application.cacheDir, UUID.randomUUID().toString())
        val cache = Cache(cacheDir, 10 * 1024 * 1024)

        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGeneralService(gson: Gson, @Named("real") okHttpClient: OkHttpClient): TokenService {
        val baseUrl = getUrl(true)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build().create(TokenService::class.java)
    }

    @Provides
    @Singleton
    fun getSharedPreferences(context: Context): AppPreference {
        return AppPreference(context)
    }

    @Provides
    @Singleton
    fun getResourceProvider(context: Context): AppResourceProvider {
        return AppResourceProvider(context)
    }

    private fun getUrl(isApi: Boolean): String {
        return if (isApi) BuildConfig.Users_URL else BuildConfig.Base_URL
    }
}