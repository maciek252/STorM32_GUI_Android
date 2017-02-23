
//import fastparse.noApi._
//import White._
import scala.io.Source

import fastparse.all._

//import fastparse.noApi._
//import White._

object Hi {
  def main(args: Array[String]) = {
    println("Hii i!")

    //val parseA = P( "{" ~ (AnyChar.rep ~ "name" ~ AnyChar.rep).! ~ "}" )

    val wartosc = (!("," | "'" | "[" | "]" | "@")
      //| "["
      //| "[" | "]"
      ~ AnyChar).rep(1).!

    //val wartoscWApostrofach = "'" ~ ((!"," ~ AnyChar).rep).! ~ "'"
    val wartoscWApostrofach = ("'" ~ wartosc ~ "'")
    //val kwadratoweNawiasy =  "[" ~  (wartosc ~ ",".?).rep(1) ~ "]" ;
    val kwadratoweNawiasy = "[" ~ (wartosc ~ ",".?).rep(max = 10) ~ "]"
    val opcjaParametru = ((!("=" | "}" | ",") ~ AnyChar).rep(1).! ~ " ".rep ~ "=>" ~ " ".rep ~ (
      kwadratoweNawiasy | wartoscWApostrofach | wartosc) ~ (" " | "," | "\n").rep)

    val parseA = P(("{" ~ (" " | "\n").rep ~ //"name" ~ " ".rep ~ "=>"  ~ " ".rep ~ "'" ~ ((!"'" ~ AnyChar).rep).! ~  "'" ~ (" "|","|"\n").rep
      /*
       ~ "type" ~ " ".rep ~ "=>"  ~ " ".rep ~ "'" ~ ((!"'" ~ AnyChar).rep).! ~  "'"  ~ (" "|","|"\n").rep
       ~ "len" ~ " ".rep ~ "=>"  ~ " ".rep  ~ ((!"," ~ AnyChar).rep).!   ~ (" "|","|"\n").rep
       ~ "ppos" ~ " ".rep ~ "=>"  ~ " ".rep  ~ ((!"," ~ AnyChar).rep).!   ~ (" "|","|"\n").rep
       ~ "min" ~ " ".rep ~ "=>"  ~ " ".rep  ~ ((!"," ~ AnyChar).rep).!   ~ (" "|","|"\n").rep
       ~ "max" ~ " ".rep ~ "=>"  ~ " ".rep  ~ ((!"," ~ AnyChar).rep).!   ~ (" "|","|"\n").rep
       ~ "default" ~ " ".rep ~ "=>"  ~ " ".rep  ~ ((!"," ~ AnyChar).rep).!   ~ (" "|","|"\n").rep
       ~ "steps" ~ " ".rep ~ "=>"  ~ " ".rep  ~ ((!"," ~ AnyChar).rep).!   ~ (" "|","|"\n").rep
       ~ "size" ~ " ".rep ~ "=>"  ~ " ".rep  ~ ((!"," ~ AnyChar).rep).!   ~ (" "|","|"\n").rep
       ~ ("adr" ~ " ".rep ~ "=>"  ~ " ".rep  ~ ((!"," ~ AnyChar).rep).!   ~ (" "|","|"\n").rep).?
       */
      // option params x => b or x => 'b'
      //((  ((!("="|"}") ~ AnyChar).rep).! ~ " ".rep ~ "=>"  ~ " ".rep ~ "'" ~ ((!("'") ~ AnyChar).rep).! ~  "'"  ~ (" "|","|"\n").rep ) |
      opcjaParametru.rep(1)
      //~ (" "|","|"\n").rep
      ~ ("##" ~ ((!"}" ~ AnyChar).rep)).!.?
      //       ~ "len" ~ " ".rep ~ "=>"  ~ " ".rep  ~ ((!"," ~ AnyChar).rep).! ~     (" "|","|"\n").rep
      //~ "ppos" ~ " ".rep ~ "=>"  ~ " ".rep ~ "'" ~ ((!"'" ~ AnyChar).rep).! ~  "'"  ~  "'" ~ (" "|","|"\n").rep
      //   ~  (!"}" ~ AnyChar).rep
      ~ "}" ~ (" " | "," | "\n").rep).rep(1))
    //val parseB = P("{" ~ ("name" ~ (!"}" ~ AnyChar).rep).! ~ "}")

    val tekst0: String = """{
  name => 'Firmware Version',
  type => 'OPTTYPE_STR+OPTTYPE_READONLY', len => 0, ppos => 0, min => 0, max => 0, steps => 0,
  size => 16,
}"""

    val tekst1: String = """{
      name => 'errrsmok',
       type => 'OPTTYPE_UI', len => 3, ppos => 4, min => 0, max => 8000, default => 500, steps => 50,
        size => 2,
        adr => 8,
      },{
        name => 'Script1 Control',
        type => 'OPTTYPE_LISTA', len => 0, ppos => 0, min => 0, max => $FunctionInputMax, default => 0, steps => 1,
        size => 1,
        adr => $CMD_g_PARAMETER_ZAHL-5, #119,
        choices => \@FunctionInputChoicesList,
        expert=> 8,
        column=> 1,
      },{
  name => 'Pitch P',
  type => 'OPTTYPE_SI', len => 5, ppos => 2, min => 0, max => 3000, default => 400, steps => 10,
  size => 2,
  adr => 0,
  pos=> [2,1],
},{
  name => 'Yaw Motor Vmax',
  type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 255, default => 150, steps => 1,
  size => 2,
  adr => 15,

##--- PAN tab --------------------
},{
        name => 'Roll D',
        type => 'OPTTYPE_UI', len => 3, ppos => 4, min => 0, max => 8000, default => 500, steps => 50,
        size => 2,
        adr => 8,
      },{
        name => 'Roll Motor Vmax',
        type => 'OPTTYPE_UI', len => 5, ppos => 0, min => 0, max => 255, default => 150, steps => 1,
        size => 2,
        adr => 9,
      }
      """
    val tekst2: String = """{
      name => 'Roll D',
      type => 'OPTTYPE_UI', len => 3, ppos => 4, min => 0, max => 8000, default => 500, steps => 50,
      size => 2,
      adr => 8,
    }"""

    /*
  type => 'OPTTYPE_UI', len => 7, ppos => 1, min => 0, max => 20000, default => 1000, steps => 50,
  size => 2,
  adr => 7,
},{
  name => 'Roll D',
  type => 'OPTTYPE_UI', len => 3, ppos => 4, min => 0, max => 8000, default => 500, steps => 50,
  size => 2,
  adr => 8,
}"""*/
    //val filename = "o323BGCTool_v096.pl"
    val filename = "v096.pl"
    //val filename = ".ensime"
    val fileContents = Source.fromFile(filename, "ISO-8859-1").getLines.mkString
    val lines = Source.fromFile(filename, "ISO-8859-1").getLines
    for (l <- lines) {
      println(l)
    }
    //println("plik=" + fileContents)

    //val Parsed.Success(value, successIndex) = parseA.parse(tekst0)
    //val value = parseA.parse(fileContents)
    val Parsed.Success(value, successIndex) = parseA.parse(fileContents)
    //assert(value == (), successIndex == 1)
    //println("successIndex=" + successIndex)
    //println("val=" + value);

    //for(v <- value){
    println(value.getClass.getSimpleName)
    val v = value.toIterator
    //while (v.hasNext)
    //      println (v.next)

    //value foreach println
    for (v <- value) {
      println("----------------------------------------\n")
      println(v._2)

      for (vv <- v._1)
        println(vv)
      //println(v._1(1))
    }
    println("lacznie parametrow: " + v.length)
    //println (value(0)._2.length)

    //for( r <- value(0)._1)
    //  println (r  + "==============\n")

    //println("-----\n" + v + "=================\n")
    //}

    /*
val failure = parseA.parse("b").asInstanceOf[Parsed.Failure]
assert(
  failure.lastParser == ("a": P0),
  failure.index == 0,
  failure.extra.traced.trace == """parseA:1:1 / "a":1:1 ..."b""""
)*/
  }
}
