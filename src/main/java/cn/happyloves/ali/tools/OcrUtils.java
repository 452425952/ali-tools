package cn.happyloves.ali.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zc
 * @date 2021/7/7 23:00
 */
@Slf4j
public final class OcrUtils {

    public static void main(String[] args) {
        System.out.println(idCard("xxxx", "xxxx", false));
    }

    /**
     * @param img     图片二进制数据的base64编码/图片url
     * @param appcode 应用Code
     * @param isFront 是否是正面
     * @return 返回值
     */
    public static String idCard(String img, String appcode, boolean isFront) {
        String host = "http://dm-51.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_idcard.json";
        Map<String, String> headers = new HashMap<>(4);
        headers.put("Authorization", "APPCODE " + appcode);
        headers.put("Content-Type", "application/json; charset=UTF-8");
        JSONObject bodyJson = JSONUtil.createObj()
                .putOpt("image", img)
                .putOpt("configure", JSONUtil.createObj()
                        .putOpt("side", isFront ? "face" : "back")
                        .putOpt("ignore_exif", "false"));
        //链式构建请求
        final String body = HttpRequest.post(host + path)
                .headerMap(headers, true)
                .body(bodyJson.toString())
                .timeout(20000)
                .execute().body();
        if (JSONUtil.isJson(body)) {
            JSONObject jsonObject = JSONUtil.parseObj(body);
            final Boolean success = jsonObject.getBool("success");
            if (!success) {
                log.error(body);
            }
        } else {
            log.error(body);
        }
        return body;
    }
}
