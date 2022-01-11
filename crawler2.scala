// This version of the crawler only
// checks links in the "domain" urbanc

import io.Source
import scala.util.matching.Regex
import scala.util._

// gets the first 10K of a web-page
def get_page(url: String) : String = {
  Try(Source.fromURL(url)("ISO-8859-1").take(10000).mkString). 
    getOrElse { println(s"  Problem with: $url"); ""}
}

// regexes for URLs and "my" domain
val http_pattern = """"https?://[^"]*"""".r
val my_urls = """urban""".r       /*@\label{myurlline}@*/
//val my_urls = """kcl.ac.uk""".r 

def unquote(s: String) = s.drop(1).dropRight(1)

def get_all_URLs(page: String) : Set[String] = 
  http_pattern.findAllIn(page).map(unquote).toSet

def crawl(url: String, n: Int) : Unit = {
  if (n == 0) ()                   /*@\label{changestartline}@*/
  else if (my_urls.findFirstIn(url) == None) { 
    println(s"Visiting: $n $url")
    get_page(url); () 
  }                                /*@\label{changeendline}@*/
  else {
    println(s"Visiting: $n $url")
    for (u <- get_all_URLs(get_page(url)).par) crawl(u, n - 1)
  }
}

// starting URL for the crawler
val startURL = """https://nms.kcl.ac.uk/christian.urban/"""
//val startURL = """https://nms.kcl.ac.uk/christian.urban/bsc-projects-17.html"""


// can now deal with depth 3 and beyond
crawl(startURL, 3)


