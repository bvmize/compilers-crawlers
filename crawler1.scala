// A crawler which checks whether there are 
// dead links in web-pages

import io.Source
import scala.util.matching.Regex
import scala.util._

// gets the first 10K of a web-page
def get_page(url: String) : String = {
  Try(Source.fromURL(url)("ISO-8859-1").take(10000).mkString).
    getOrElse { println(s"  Problem with: $url"); ""}
}
get_page("https://nms.kcl.ac.uk/christiana.urban/")
// regex for URLs
val http_pattern = """"https?://[^"]*"""".r /*@\label{httpline}@*/ 

// drops the first and last character from a string
def unquote(s: String) = s.drop(1).dropRight(1)

def get_all_URLs(page: String) : Set[String] = 
  http_pattern.findAllIn(page).map(unquote).toSet /*@\label{findallline}@*/

// naive version of crawl - searches until a given depth,
// visits pages potentially more than once
def crawl(url: String, n: Int) : Unit = {
  if (n == 0) ()
  else {
    println(s"Visiting: $n $url")
    for (u <- get_all_URLs(get_page(url))) crawl(u, n - 1)
  }
}

// some starting URLs for the crawler
val startURL = """https://nms.kcl.ac.uk/christian.urban/"""
//val startURL = """https://nms.kcl.ac.uk/luc.moreau/"""

crawl(startURL, 2)

