/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.springframework.util.Assert;

/**
 * Utility class for URI encoding and decoding based on RFC 3986. Offers encoding methods for the various URI
 * components.
 *
 * <p>All {@code encode*(String, String} methods in this class operate in a similar way:
 * <ul>
 *     <li>Valid characters for the specific URI component as defined in RFC 3986 stay the same.</li>
 *     <li>All other characters are converted into one or more bytes in the given encoding scheme. Each of the
 *     resulting bytes is written as a hexadecimal string in the "<code>%<i>xy</i></code>" format.</li>
 * </ul>
 *
 * @author Arjen Poutsma
 * @see <a href="http://www.ietf.org/rfc/rfc3986.txt">RFC 3986</a>
 * @since 3.0
 */
public abstract class UriUtils {

	// encoding

	/**
	 * Encodes the given source URI into an encoded String. All various URI components are encoded according to their
	 * respective valid character sets.
	 *
	 * @param uri the URI to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded URI
	 * @throws IllegalArgumentException when the given uri parameter is not a valid URI
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeUri(String uri, String encoding) throws UnsupportedEncodingException {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(uri).build();
        UriComponents encoded = uriComponents.encode(encoding);
        return encoded.toUriString();
    }

	/**
	 * Encodes the given HTTP URI into an encoded String. All various URI components are encoded according to their
	 * respective valid character sets. <p><strong>Note</strong> that this method does not support fragments ({@code #}),
	 * as these are not supposed to be sent to the server, but retained by the client.
	 *
	 * @param httpUrl the HTTP URL to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded URL
	 * @throws IllegalArgumentException when the given uri parameter is not a valid URI
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeHttpUrl(String httpUrl, String encoding) throws UnsupportedEncodingException {
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(httpUrl).build();
        UriComponents encoded = uriComponents.encode(encoding);
        return encoded.toUriString();
	}

	/**
	 * Encodes the given source URI components into an encoded String. All various URI components are optional, but encoded
	 * according to their respective valid character sets.
	 *
	 * @param scheme the scheme
	 * @param authority the authority
	 * @param userInfo  the user info
	 * @param host the host
	 * @param port the port
	 * @param path the path
	 * @param query the query
	 * @param fragment the fragment
	 * @param encoding the character encoding to encode to
	 * @return the encoded URI
	 * @throws IllegalArgumentException when the given uri parameter is not a valid URI
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeUriComponents(String scheme,
											 String authority,
											 String userInfo ,
											 String host,
											 String port,
											 String path,
											 String query,
											 String fragment,
											 String encoding) throws UnsupportedEncodingException {
		int portAsInt = port != null ? Integer.parseInt(port) : -1;

		UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
		builder.scheme(scheme).userInfo(userInfo).host(host).port(portAsInt);
		builder.path(path).query(query).fragment(fragment);

		UriComponents encoded = builder.build().encode(encoding);

        return encoded.toUriString();
	}

	// encoding convenience methods

	/**
	 * Encodes the given URI scheme with the given encoding.
	 *
	 * @param scheme the scheme to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded scheme
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeScheme(String scheme, String encoding) throws UnsupportedEncodingException {
		return UriComponents.encodeUriComponent(scheme, encoding, UriComponents.Type.SCHEME);
	}

	/**
	 * Encodes the given URI authority with the given encoding.
	 *
	 * @param authority the authority to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded authority
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeAuthority(String authority, String encoding) throws UnsupportedEncodingException {
		return UriComponents.encodeUriComponent(authority, encoding, UriComponents.Type.AUTHORITY);
	}

	/**
	 * Encodes the given URI user info with the given encoding.
	 *
	 * @param userInfo the user info to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded user info
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeUserInfo(String userInfo, String encoding) throws UnsupportedEncodingException {
		return UriComponents.encodeUriComponent(userInfo, encoding, UriComponents.Type.USER_INFO);
	}

	/**
	 * Encodes the given URI host with the given encoding.
	 *
	 * @param host the host to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded host
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeHost(String host, String encoding) throws UnsupportedEncodingException {
		return UriComponents.encodeUriComponent(host, encoding, UriComponents.Type.HOST);
	}

	/**
	 * Encodes the given URI port with the given encoding.
	 *
	 * @param port the port to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded port
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodePort(String port, String encoding) throws UnsupportedEncodingException {
		return UriComponents.encodeUriComponent(port, encoding, UriComponents.Type.PORT);
	}

	/**
	 * Encodes the given URI path with the given encoding.
	 *
	 * @param path the path to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded path
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodePath(String path, String encoding) throws UnsupportedEncodingException {
		return UriComponents.encodeUriComponent(path, encoding, UriComponents.Type.PATH);
	}

	/**
	 * Encodes the given URI path segment with the given encoding.
	 *
	 * @param segment the segment to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded segment
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodePathSegment(String segment, String encoding) throws UnsupportedEncodingException {
		return UriComponents.encodeUriComponent(segment, encoding, UriComponents.Type.PATH_SEGMENT);
	}

	/**
	 * Encodes the given URI query with the given encoding.
	 *
	 * @param query the query to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded query
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeQuery(String query, String encoding) throws UnsupportedEncodingException {
		return UriComponents.encodeUriComponent(query, encoding, UriComponents.Type.QUERY);
	}

	/**
	 * Encodes the given URI query parameter with the given encoding.
	 *
	 * @param queryParam the query parameter to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded query parameter
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeQueryParam(String queryParam, String encoding) throws UnsupportedEncodingException {
		return UriComponents.encodeUriComponent(queryParam, encoding, UriComponents.Type.QUERY_PARAM);
	}

	/**
	 * Encodes the given URI fragment with the given encoding.
	 *
	 * @param fragment the fragment to be encoded
	 * @param encoding the character encoding to encode to
	 * @return the encoded fragment
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 */
	public static String encodeFragment(String fragment, String encoding) throws UnsupportedEncodingException {
		return UriComponents.encodeUriComponent(fragment, encoding, UriComponents.Type.FRAGMENT);
	}


	// decoding

	/**
	 * Decodes the given encoded source String into an URI. Based on the following rules:
	 * <ul>
	 *     <li>Alphanumeric characters {@code "a"} through {@code "z"}, {@code "A"} through {@code "Z"}, and
	 *     {@code "0"} through {@code "9"} stay the same.</li>
	 *     <li>Special characters {@code "-"}, {@code "_"}, {@code "."}, and {@code "*"} stay the same.</li>
	 *     <li>A sequence "<code>%<i>xy</i></code>" is interpreted as a hexadecimal representation of the character.</li>
 	 * </ul>
	 *
	 * @param source the source string
	 * @param encoding the encoding
	 * @return the decoded URI
	 * @throws IllegalArgumentException when the given source contains invalid encoded sequences
	 * @throws UnsupportedEncodingException when the given encoding parameter is not supported
	 * @see java.net.URLDecoder#decode(String, String)
	 */
	public static String decode(String source, String encoding) throws UnsupportedEncodingException {
		Assert.notNull(source, "'source' must not be null");
		Assert.hasLength(encoding, "'encoding' must not be empty");
		int length = source.length();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(length);
		boolean changed = false;
		for (int i = 0; i < length; i++) {
			int ch = source.charAt(i);
			if (ch == '%') {
				if ((i + 2) < length) {
					char hex1 = source.charAt(i + 1);
					char hex2 = source.charAt(i + 2);
					int u = Character.digit(hex1, 16);
					int l = Character.digit(hex2, 16);
					if (u == -1 || l == -1) {
						throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
					}
					bos.write((char) ((u << 4) + l));
					i += 2;
					changed = true;
				}
				else {
					throw new IllegalArgumentException("Invalid encoded sequence \"" + source.substring(i) + "\"");
				}
			}
			else {
				bos.write(ch);
			}
		}
		return changed ? new String(bos.toByteArray(), encoding) : source;
	}

}