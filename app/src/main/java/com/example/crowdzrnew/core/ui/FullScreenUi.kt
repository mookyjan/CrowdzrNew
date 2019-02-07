package com.example.crowdzrnew.core.ui

import android.databinding.ObservableBoolean
import android.os.Parcelable
import com.example.crowdzrnew.R
import com.example.crowdzrnew.utilities.observables.ObservableBackground
import kotlinx.android.parcel.Parcelize

enum class FullScreenType {

    SERVERERROR,
    NOCONNECTION,
    ACCOUNTCREATED
}

@Parcelize
data class FullScreenUi(val screenType: FullScreenType) : Parcelable {


    val hasCommon = ObservableBoolean()

    val image = ObservableBackground()

    init {
        when (screenType) {



            FullScreenType.NOCONNECTION -> {
                image.setDrawableResource(R.drawable.illus_error_connection)
            }
            FullScreenType.SERVERERROR -> {
                image.setDrawableResource(R.drawable.illus_error_server)
            }

            FullScreenType.ACCOUNTCREATED -> {
                image.setDrawableResource(R.drawable.illus_account_create)
            }
        }

    }

    fun getSubTitle(): Int {
        when (screenType) {

            FullScreenType.NOCONNECTION -> {

                hasCommon.set(true)
                return R.string.no_conn_subtitle


            }
            FullScreenType.SERVERERROR -> {

                hasCommon.set(true)
                return R.string.server_error_subtitle

            }

            FullScreenType.ACCOUNTCREATED -> {
                hasCommon.set(true)
                return R.string.create_account_success_subtitle

            }
        }
        return R.string.empty
    }


    fun getTitle(): Int {

        when (screenType) {


            FullScreenType.NOCONNECTION -> {

                return R.string.no_connection


            }
            FullScreenType.SERVERERROR -> {

                return R.string.server_err

            }

            FullScreenType.ACCOUNTCREATED -> {

                return R.string.create_account_success

            }
        }
    }

    fun getButtonText(): Int {
        when (screenType) {


            FullScreenType.NOCONNECTION -> {

                return R.string.title_try_again

            }
            FullScreenType.SERVERERROR -> {

                return R.string.title_ok

            }

            FullScreenType.ACCOUNTCREATED -> {

                return R.string.title_dashboard

            }
        }
    }
}
