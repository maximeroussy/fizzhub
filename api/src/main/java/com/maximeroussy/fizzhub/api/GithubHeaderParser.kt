package com.maximeroussy.fizzhub.api

import okhttp3.Headers
import java.net.URL
import java.util.Collections
import javax.inject.Inject

class GithubHeaderParser @Inject constructor() {
  fun getNextPage(headers: Headers): Int {
    val link = headers.get("Link")

    if (link != null) {
      val linkParts = link.split(",")
      linkParts.forEach {
        if (getLinkRelation(it) == "next") {
          return getPageNumber(getLink(it))
        }
      }
    }
    return -1
  }

  private fun getLinkRelation(linkHeaderPart: String): String {
    val length = linkHeaderPart.length
    return linkHeaderPart.substring(length - 5, length - 1)
  }

  private fun getLink(linkHeaderPart: String): URL {
    val length = linkHeaderPart.length
    val firstCharIndex = linkHeaderPart.indexOf("<")
    return URL(linkHeaderPart.substring(firstCharIndex + 1, length - 13))
  }

  private fun getPageNumber(link: URL): Int {
    val pageNumberString = splitQuery(link.query)["page"]
    return if (!pageNumberString.isNullOrEmpty()) {
      pageNumberString!!.toInt()
    } else {
      -1
    }
  }

  private fun splitQuery(query: String): Map<String, String> {
    if (query.isEmpty()) {
      return Collections.emptyMap()
    }

    val queryMap = HashMap<String, String>()
    val queryStringList = query.split("&")
    queryStringList.forEach {
      val equalsSymbolPosition = it.indexOf("=")
      val name = it.substring(0, equalsSymbolPosition)
      val value = it.substring(equalsSymbolPosition + 1, it.length)
      queryMap[name] = value
    }

    return queryMap
  }
}
