# If you can see camera button on Browser, when you change user-agent to mobile.
# You should set hoge.jpg image to same directory with this file or change `photo=@hoge.jpg` to image path that you want to upload.
# Please set instagram username and password.

# Login Info
USER_NAME=""
PASSWORD=""
USER_AGENT="Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1"

function curl_req() {
    echo "$1"
    eval "$1"
    CSRF_TOKEN=$(cat cookie.txt | grep csrftoken | awk '{print $7}')
    sleep 3s
}

# Top page
CURL_TOP="curl -c cookie.txt -X GET \
-H 'User-Agent: ${USER_AGENT}' \
'https://www.instagram.com/'"
curl_req "$CURL_TOP"

# Login
CURL_LOGIN="curl -i -s -k -X 'POST' \
-H 'Host: www.instagram.com' \
-H 'User-Agent: ${USER_AGENT}' \
-H 'Accept: */*' \
-H 'Accept-Language: ar,en-US;q=0.7,en;q=0.3' \
-H 'Accept-Encoding: gzip, deflate, br' \
-H 'X-CSRFToken: ${CSRF_TOKEN}' \
-H 'X-Instagram-AJAX: 1' \
-H 'Content-Type: application/x-www-form-urlencoded' \
-H 'X-Requested-With: XMLHttpRequest' \
-H 'Referer: https://www.instagram.com/' \
-b cookie.txt \
-c cookie.txt \
--data 'username=${USER_NAME}&password=${PASSWORD}' \
'https://www.instagram.com/accounts/login/ajax/'"
curl_req "$CURL_LOGIN"

# Top page again
CURL_TOP="curl -b cookie.txt -X GET \
-H 'User-Agent: ${USER_AGENT}' \
'https://www.instagram.com/'"
curl_req "$CURL_TOP"

# Upload image file
UPLOAD_ID="$(date +%s)000"
CURL_CREATE_UPLOAD_PHOTO="curl -v \
-H 'Host: www.instagram.com' \
-H 'X-Requested-With: XMLHttpRequest' \
-H 'X-Instagram-AJAX: 1' \
-H 'Accept: */*' \
-H 'User-Agent: ${USER_AGENT}' \
-H 'X-CSRFToken: ${CSRF_TOKEN}' \
-H 'Referer: https://www.instagram.com/create/crop/' \
-H 'Accept-Encoding: gzip, deflate, br' \
-H 'Accept-Language: ja,en-US;q=0.8,en;q=0.6' \
-F 'upload_id=${UPLOAD_ID}' \
-F 'photo=@hoge.jpg' \
-F 'media_type=1' \
-b cookie.txt \
-c cookie.txt \
'https://www.instagram.com/create/upload/photo/'"
curl_req "$CURL_CREATE_UPLOAD_PHOTO"

# Share image
CURL_CREATE_CONFIGURE="curl -i -s -k -X 'POST' \
-H 'Host: www.instagram.com' \
-H 'X-Requested-With: XMLHttpRequest' \
-H 'X-Instagram-AJAX: 1' \
-H 'Content-Type: application/x-www-form-urlencoded' \
-H 'Accept: */*' \
-H 'User-Agent: ${USER_AGENT}' \
-H 'X-CSRFToken: ${CSRF_TOKEN}' \
-H 'Referer: https://www.instagram.com/create/details/' \
-H 'Accept-Encoding: gzip, deflate, br' \
-H 'Accept-Language: ar,en-US;q=0.7,en;q=0.3' \
-b cookie.txt \
-c cookie.txt \
--data 'upload_id=${UPLOAD_ID}&caption=test' \
'https://www.instagram.com/create/configure/'"
curl_req "$CURL_CREATE_CONFIGURE"