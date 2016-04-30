package com.n9mtq4.translation

/**
 * Created by will on 4/16/16 at 8:06 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class SyntaxError(val msg: String, val line: String) : Exception("$msg with line: '$line'")
