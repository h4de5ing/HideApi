package com.android.hideapi

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.textView).setOnClickListener {
            try {
                val type = "1"
                val ip = "10.18.16.3"
                val netmask = "255.255.255.0"
                val gateway = "10.18.16.1"
                val dns1 = "10.18.16.2"
                val dns2 = "10.18.16.3"
                setEthernetIp(
                    mode = if (type == "1") "STATIC" else "DHCP",
                    ip = ip,
                    netLength = countLengthInNetMask(netmask),
                    gateway = gateway,
                    dns = listOf(dns1, dns2),
                    proxyType = "STATIC",
                    proxy = "127.0.0.1",
                    port = 0,
                    exclList = listOf(),
                    pacUri = ""
                ) {
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    //计算子网掩码长度
    fun countLengthInNetMask(ipAddress: String): Int {
        try {
            if (ipAddress == "") return 0
            val parts = ipAddress.split(".")
            val binaryStringBuilder = StringBuilder()
            for (part in parts) {
                val binaryString = Integer.toBinaryString(part.toInt())
                binaryStringBuilder.append(binaryString.padStart(8, '0'))
            }
            val binaryString = binaryStringBuilder.toString()
            var count = 0
            for (char in binaryString) {
                if (char == '1') count++
                else break
            }
            return count
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }
}