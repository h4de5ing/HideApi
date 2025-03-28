package com.android.hideapi

import android.net.IpConfiguration

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
    val assignment =
        if (mode == "STATIC") IpConfiguration.IpAssignment.STATIC else IpConfiguration.IpAssignment.DHCP
}