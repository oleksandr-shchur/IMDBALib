/*******************************************************************************
 * @fileOverview  ParserUtils.java
 * @author Oleksandr Shchur
 * @date Jan 15, 2015
 * @param (C) Oleksandr Shchur alex.v.schur(at)gmail.com
 * @param You are free to use this source code for personal or commercial use.
 ******************************************************************************/
package parsers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Class ParserUtils.Holds some static parsers. 
 * @author Oleksandr Shchur
 */
public class ParserUtils {


	/**
	 * IMDB offline files uses special format that is common. 
	 * String values are separated by TAB symbols. 
	 * This class do the job by converting such strings in more structured presentation.
	 * the first string is the key (film title), and other are processed as the Set of strings (holding certificates)
	 * Parses file type that are common in IMDB formst.
	 * Example :
	 * 
	 * Yolanda and the Thief (1945)				Sweden:Btl
	 * Yolanda and the Thief (1945)				USA:Approved	(PCA #10928)
	 * Yolda - R¹zgar geri getirirse (2005)			Singapore:PG
	 * Yolngu Boy (2001)					Australia:M
	 * Yolo Babes: The Take Over (2012) (V)			USA:Not Rated
	 * Yom Yom (1998)						Argentina:13
	 * Yom Yom (1998)						France:U
	 * Yome no nedoko: hajishirazuna uzuki (2013)		Japan:R18+
	 * Yomei 1-kagetsu no hanayome (2009)			Hong Kong:IIA
	 * Yomei 1-kagetsu no hanayome (2009)			Japan:G
	 * Yomei 1-kagetsu no hanayome (2009)			Singapore:PG
	 * Yomigaeri (2002)					South Korea:All
	 * Yomigaeri no chi (2009)					Japan:PG-12
	 * 
	 * @param path A path to IMDB without ending slash, e.g. C:\IMDB
	 * @param fileName filename of a file to parse, e.g. certificates.list
	 * @param offset
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Set<String>> parseFile(byte[] dat, int offset) {
		Map<String, Set<String>> res = new HashMap<String, Set<String>>();
		byte ch;
		int startPosition = offset;

		// search the entire file
		int size = dat.length;
		int pos = startPosition;
		String movieTitle;
		StringBuilder certificate = new StringBuilder();

		while (pos < size) {
			int startPos = pos;
			// search the first TAB symbol 
			while (++pos < size && (ch = dat[pos]) != 0x09);

			if (pos >= size) {
				break;
			}

			// get film title
			movieTitle = new String(dat, startPos+1, pos-startPos-1); 

			// skip TABs
			while ((ch = dat[++pos]) == 0x09);

			// get item (certificate or keyword, etc.) string
			certificate.append((char)ch);
			while ((ch = dat[++pos]) != 0x0A) {
				certificate.append((char)ch);
			}
			// here we have film and item strings

			// let's merge in datastrucrure
			if (res.containsKey(movieTitle)) {
				res.get(movieTitle).add(certificate.toString());
			} else {
				Set<String> certificates = new HashSet<String>();
				certificates.add(certificate.toString());
				res.put(movieTitle, certificates);
			}
			certificate.setLength(0);
		} 
		return res;
	}

}
