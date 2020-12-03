package elkutils

object Models {

  case class Test1Line(var userId: Int, var movieId: Int, var tag: String, var timestamp: Long) {
    def this() {
      this(-1, -1, "nothing", -1)
    }
  }

}
