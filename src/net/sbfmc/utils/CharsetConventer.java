/*
 Copyright 2014 Reo_SP

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 	http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package net.sbfmc.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mozilla.universalchardet.UniversalDetector;

public class CharsetConventer {
	private final static String windows1251Chars = "�ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö÷øùúûüýþÿ";
	private final static String utf8Chars = "?АБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнорстуфхцчшщъыьэюяп";
	private final static Pattern yamlNotPrintable = Pattern.compile("[^\t\n\r\u0020-\u007E\u0085\u00A0-\uD7FF\uE000-\uFFFD]");

	public static String toUTF8(String string) {
		return toUTF8_CharsEncode(string);
	}

	public static String toUTF8_CharsEncode(String string) {
		String src = string;
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < src.length(); i++) {
			char curChar = src.charAt(i);
			int tablePos = windows1251Chars.indexOf(curChar);
			if (tablePos == -1) {
				stringBuilder.append(curChar);
			} else {
				stringBuilder.append(utf8Chars.charAt(tablePos));
			}
		}

		return stringBuilder.toString();
	}

	public static String toUTF8_BytesEncode(String string) {
		try {
			Class.forName("org.mozilla.universalchardet.UniversalDetector");

			byte[] stringBytes = string.getBytes();

			UniversalDetector detector = new UniversalDetector(null);
			detector.handleData(stringBytes, 0, stringBytes.length - 1);
			detector.dataEnd();

			Charset srcCharset = Charset.forName(detector.getDetectedCharset());
			Charset dstCharset = Charset.forName("UTF-8");
			CharBuffer srcEncoded = srcCharset.decode(ByteBuffer.wrap(stringBytes));
			byte[] dstEncoded = dstCharset.encode(srcEncoded).array();

			return new String(dstEncoded, dstCharset.displayName());
		} catch (Exception err) {
			return string;
		}
	}

	public static void fileToUTF8(File file) {
		try {
			String text = "";
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String s;
			while ((s = reader.readLine()) != null) {
				text += s + "\n";
			}
			reader.close();

			text = toUTF8(text);
			text = repairForYaml(text);

			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(text);
			writer.close();
		} catch (IOException err) {
			err.printStackTrace();
		}
	}

	public static String repairForYaml(String src) {
		Matcher matcher = yamlNotPrintable.matcher(src);
		if (matcher.find()) {
			return matcher.replaceAll("");
		}
		return src;
	}
}
