package com.n9mtq4.translation

import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.MessageFormat
import java.util.HashMap

/**
 * Created by will on 4/16/16 at 7:15 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class Translation(val lang: String) : HashMap<String, String>() {
	
	operator fun get(key: String, vararg values: Any?) = MessageFormat.format(get(key)!!, *values)
	
}

fun translation(dir: String = "/lang", code: String = System.getProperty("user.language")) = translation("$dir/$code.lang")
fun translation(file: String): Translation {
	
	val input = Translation::class.java.getResourceAsStream(file)
	val reader = BufferedReader(InputStreamReader(input))
	
	val tr = TranslationUtils.makeTranslations(reader)
	
	reader.close()
	input.close()
	
	return tr
	
}
