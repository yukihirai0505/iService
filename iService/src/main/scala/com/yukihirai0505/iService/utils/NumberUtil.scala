package com.yukihirai0505.iService.utils

/**
  * Created by Yuky on 2017/09/28.
  */
object NumberUtil {
  def getRandomInt(min: Int = 2000, max: Int = 10000): Long = {
    (Math.floor(Math.random() * (max - min + 1)) + min).toLong
  }
}
