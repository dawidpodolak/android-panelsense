package com.nspanel.data.icons

import java.nio.charset.StandardCharsets

class PathData(@kotlin.jvm.JvmField var data: ByteArray, @kotlin.jvm.JvmField var color: Int) {
    constructor(data: String, color: Int) : this(data.toByteArray(StandardCharsets.UTF_8), color)
}
