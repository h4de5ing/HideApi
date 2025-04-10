package com.android.hideapi

import android.net.IEthernetManager
import android.net.IpConfiguration
import android.net.LinkAddress
import android.net.NetworkUtils
import android.net.ProxyInfo
import android.net.StaticIpConfiguration
import android.net.Uri
import android.os.INetworkManagementService
import android.os.ServiceManager
import java.net.InetAddress
import java.net.UnknownHostException

fun numericToInetAddress(numericAddress: String = "127.0.0.1"): InetAddress {
    return try {
//        InetAddress.getByName(numericAddress)
        NetworkUtils.numericToInetAddress(numericAddress)
    } catch (e: UnknownHostException) {
        throw IllegalArgumentException("Invalid IP address format: $numericAddress", e)
    }
}

fun setEthernetIp(
    mode: String,//IpConfigurationIpAssignment  0-static  1-DHCP 2-UNASSIGNED
    ip: String,
//    mask: String,//用网络长度代替子网掩码
    netLength: Int,
    gateway: String,
    dns: List<String>,
    proxyType: String,//IpConfiguration.ProxySettings.ProxySettings 0-NONE 1-STATIC 2-UNASSIGNED 3-PAC
    proxy: String,
    port: Int,
    exclList: List<String>,
    pacUri: String,
    listener: ((String) -> Unit) = {}
) {
    try {
        val iface = "eth0"
        val eth = IEthernetManager.Stub.asInterface(ServiceManager.getService("ethernet"))
        val net =
            INetworkManagementService.Stub.asInterface(ServiceManager.getService("network_management"))
        if (net.listInterfaces().any { it == iface }) {
            val assignment =
                if (mode == "STATIC") IpConfiguration.IpAssignment.STATIC else IpConfiguration.IpAssignment.DHCP

            val proxySettings: IpConfiguration.ProxySettings?
            var proxyInfo: ProxyInfo? = null
            when (proxyType) {
                "STATIC" -> {
                    proxySettings = IpConfiguration.ProxySettings.STATIC
                    proxyInfo = ProxyInfo.buildDirectProxy(proxy, port, exclList)
                }

                "PAC" -> {
                    proxySettings = IpConfiguration.ProxySettings.PAC
                    proxyInfo = ProxyInfo.buildPacProxy(Uri.parse(pacUri))
                }

                else -> {
                    proxySettings = IpConfiguration.ProxySettings.NONE
                }
            }

            val staticConfig = StaticIpConfiguration()
            if ("STATIC" == mode) {
                StaticIpConfiguration.Builder()
                    .setIpAddress(LinkAddress(numericToInetAddress(ip), netLength))
                    .setGateway(numericToInetAddress(gateway))
                    .build()
                dns.forEach {
                    if (it != "") staticConfig.addDnsServer(numericToInetAddress(it))
                }
            }

            val ipConfig = IpConfiguration(assignment, proxySettings, staticConfig, proxyInfo)
            eth.setConfiguration(iface, ipConfig)
            listener.invoke("success")

            //这里在其他平台不通用,如果其他平台没有这2个方法，那么设置需要重启才能生效,或者修改系统setConfiguration实现，增加removeInterface(iface)；start()；就可以实现立即生效
//            eth.Trackstop()
//            eth.Trackstart()
            //开启以太网
//            net.setInterfaceUp(iface)
            //关闭以太网
//            net.setInterfaceDown(iface)
        } else {
            listener.invoke("failed: not found ether interface:${iface}")
        }
    } catch (e: Exception) {
        listener.invoke("failed: ${e.message}")
        e.printStackTrace()
    }
}