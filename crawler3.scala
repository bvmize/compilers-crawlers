// This version of the crawler that also
// "harvests" email addresses from webpages

import io.Source
import scala.util.matching.Regex
import scala.util._

def get_page(url: String) : String = {
  Try(Source.fromURL(url)("ISO-8859-1").take(10000).mkString).
    getOrElse { println(s"  Problem with: $url"); ""}
}

// regexes for URLs, for "my" domain and for email addresses
val http_pattern = """"https?://[^"]*"""".r
val email_pattern = """([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})""".r /*@\label{emailline}@*/

def unquote(s: String) = s.drop(1).dropRight(1)

def get_all_URLs(page: String) : Set[String] = 
  http_pattern.findAllIn(page).map(unquote).toSet

def print_str(s: String) = 
  if (s == "") () else println(s)

def crawl(url: String, n: Int) : Unit = {
  if (n == 0) ()
  else {
    println(s"  Visiting: $n $url")
    val page = get_page(url)
    print_str(email_pattern.findAllIn(page).mkString("\n")) /*@\label{mainline}@*/
    for (u <- get_all_URLs(page).par) crawl(u, n - 1)
  }
}

// staring URL for the crawler
val startURL = """https://nms.kcl.ac.uk/christian.urban/"""


crawl(startURL, 3)
