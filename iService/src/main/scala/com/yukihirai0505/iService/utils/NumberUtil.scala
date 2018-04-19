package com.yukihirai0505.iService.utils

object NumberUtil {
  def getRandomInt(min: Int = 2000, max: Int = 10000): Long = {
    (Math.floor(Math.random() * (max - min + 1)) + min).toLong
  }
}
