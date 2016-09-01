import Chisel._

class RegisterFile extends Module {
  val io = new Bundle {
    val wren = Bool(INPUT)
    val addrX = UInt(INPUT, 5)
    val addrY = UInt(INPUT, 5)
    val addrD = UInt(INPUT, 5)
    val mask = UInt(INPUT, 32)
    val dataDi = UInt(INPUT, 32)
    val dataXo = UInt(OUTPUT, 32)
    val dataYo = UInt(OUTPUT, 32)
  }
  val rf = Mem(32, UInt(width = 32))
  when (io.wren) {
    rf.write(io.addrD, io.dataDi, io.mask)
  }
  io.dataXo := rf(io.addrX)
  io.dataYo := rf(io.addrY)
}


class RegisterFileTest(c: RegisterFile) extends Tester(c) {
  for (t <- 0 until 31) {
    poke(c.io.wren, true)
    poke(c.io.addrD, t)
    poke(c.io.dataDi, 1000 - t)
    poke(c.io.mask, 0xffffffff)
    step(1)
    poke(c.io.addrX, t)
    expect(c.io.dataXo, 1000 - t)
    poke(c.io.addrY, t)
    expect(c.io.dataYo, 1000 - t)
  }
}

object inst {
  def main(args: Array[String]): Unit = {
    chiselMainTest(args,
      () => Module(new RegisterFile())) {c => new RegisterFileTest(c)}
  }
}
