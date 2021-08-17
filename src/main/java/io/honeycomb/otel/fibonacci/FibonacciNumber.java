package io.honeycomb.otel.fibonacci;

public class FibonacciNumber {

  public final int index;
  public final int fibonacciNumber;

  public FibonacciNumber(int index, int fibonacciNumber) {
    this.index = index;
    this.fibonacciNumber = fibonacciNumber;
  }

  public int getIndex() {
    return index;
  }

  public int getFibonacciNumber() {
    return fibonacciNumber;
  }
}
