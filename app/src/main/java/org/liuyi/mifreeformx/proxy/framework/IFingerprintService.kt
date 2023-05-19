package org.liuyi.mifreeformx.proxy.framework

import android.os.IBinder
import android.os.RemoteException
import org.liuyi.mifreeformx.xposed.base.annotation.ProxyMethod
import org.liuyi.mifreeformx.xposed.base.interfaces.ProxyInterface
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.getProxyAs
import org.liuyi.mifreeformx.xposed.hooker.FrameworkBaseHooker.toClass

/**
 * @Author: Liuyi
 * @Date: 2023/05/19/10:39:30
 * @Description:
 */
interface IFingerprintService : ProxyInterface {

    @ProxyMethod(name = "isClientActive")
    fun isClientActive(): Boolean

    interface Stub : ProxyInterface {

        companion object {
            val StaticProxy by lazy {
                "android.hardware.fingerprint.IFingerprintService\$Stub".toClass().getProxyAs<Stub>()
            }
        }

        @ProxyMethod(name = "asInterface")
        fun asInterface(obj: IBinder?): IFingerprintService?
    }
}