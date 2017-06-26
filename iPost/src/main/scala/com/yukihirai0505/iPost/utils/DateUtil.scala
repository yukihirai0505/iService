package com.yukihirai0505.iPost.utils

/**
  * author Yuki Hirai on 2017/05/30.
  */
object DateUtil {

  /***
    * Create timestamp
     * @return
    */
  def timestamp: String = {
    (System.currentTimeMillis / 1000).toString
  }

}
