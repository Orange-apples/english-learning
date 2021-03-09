package com.cxylm.springboot.util.baidu;

import org.springframework.stereotype.Component;

/**
 * 百度AI工具类
 *
 * @author HaoTi
 * @since 2019-01-10
 */
@Component
public class BaiDuAiUtil {
//    /**
//     * 设置APPID/AK/SK
//     */
//    private final BaiDuAiProperties baiDuAIProperties;
//    private static final String ERROR_CODE = "error_code";
//    private static final Integer ERROR_CODE1 = 110;
//    private static final Integer ERROR_CODE2 = 111;
//    private static final Object SUCC_FALI = 0.0;
//    private static final Object SUCC_CODE = 1.0;
//    private static final String SUCC = "正常";
//    private static final String SUCC2 = "性感";
//
//    private static final Integer SUCCESS = 1;
//    private static final Integer FAIL = 2;
//
//    //    private String accessToken = "24.202fb7bf7bc2fa939f2c7fc9d992e7b4.2592000.1550195606.282335-15362917";
//    @Autowired
//    public BaiDuAiUtil(BaiDuAiProperties baiDuAIProperties) {
//        this.baiDuAIProperties = baiDuAIProperties;
//    }
//
//    BaiDuAiUtil() {
//        baiDuAIProperties = new BaiDuAiProperties();
//        baiDuAIProperties.setAppId("15362917");
//        baiDuAIProperties.setApiKey("RsqFu633zHg043YB3hgreELD");
//        baiDuAIProperties.setSecretKey("MMt7Px9d9Id0LmSiKQz5ZXG6O7bLVsob");
//    }
//
//    private static final Logger log = LoggerFactory.getLogger(BaiDuAiUtil.class.getClass());
//
//    /**
//     * 1 - 酒店
//     * 2 - KTV
//     * 3 - 丽人
//     * 4 - 美食餐饮
//     * 5 - 旅游
//     * 6 - 健康
//     * 7 - 教育
//     * 8 - 商业
//     * 9 - 房产
//     * 10 - 汽车
//     * 11 - 生活
//     * 12 - 购物
//     * 13 - 3C
//     */
//    /**
//     * 提取评论标签
//     * 返回参数
//     * <p>
//     * 参数	类型	描述
//     * log_id	uint64	请求唯一标识码
//     * prop	string	匹配上的属性词
//     * adj	string	匹配上的描述词
//     * sentiment	int	该情感搭配的极性（0表示消极，1表示中性，2表示积极）
//     * begin_pos	int	该情感搭配在句子中的开始位置
//     * end_pos	int	该情感搭配在句子中的结束位置
//     * abstract	string	对应于该情感搭配的短句摘要
//     */
////    public Map sampleComment(Map<Integer, Object> map) {
////        //TODO  step 1:初始化百度AI请求参数
////        // 初始化一个AipNlp
////        AipNlp client = new AipNlp(baiDuAIProperties.getAppId(), baiDuAIProperties.getApiKey(), baiDuAIProperties.getSecretKey());
////
////        // 可选：设置网络连接参数
////        client.setConnectionTimeoutInMillis(2000);
////        client.setSocketTimeoutInMillis(60000);
////        // 传入可选参数调用接口
////        HashMap<String, Object> options = new HashMap<>();
////
////        //TODO  step 2:对map集合中的内容进行处理
////        if (map != null) {
////            for (Integer storeId : map.keySet()) {
////                Map<Integer, String> comContentMap = ((Map<Integer, String>) map.get(storeId));
////
////                //TODO  step 2.1:获取商家类型（与百度AI类型对应，默认为：美食）
////                ESimnetType eSimnetType = storeService.changeStoreTypeByBaiDu(storeId);
////
////                Map<Integer, Map<String, Integer>> map1 = new HashMap<>(comContentMap.size() / 2);
////                for (Integer memberId : comContentMap.keySet()) {
////
////                    //TODO  step 2.2:转化评论为标签，转化标签内容
////                    JSONObject response = client.commentTag((String) comContentMap.get(memberId), eSimnetType, options);
////                    if (response.has("ERROR_CODE")) {
////                        break;
////                    }
////                    JSONArray jsonArray = (JSONArray) response.get("items");
////                    List list2 = jsonArray.toList();
////
////                    //TODO  step 2.3:用一个集合排除一个用户一次评论里出现的重复标签
////                    Map<String, Integer> set = new HashMap<>();
////                    for (int j = 0; j < list2.size(); j++) {
////                        Map<String, Object> map2 = (Map<String, Object>) list2.get(j);
////
////                        StringBuilder stringBuilder = new StringBuilder();
////                        stringBuilder.append(map2.get("prop"));
////                        stringBuilder.append(map2.get("adj"));
////                        stringBuilder.append(",");
////                        stringBuilder.append(map2.get("sentiment"));
////                        String tag = stringBuilder.toString();
////
////                        set.put(tag, memberId);
////                    }
////
////                    //TODO  step 2.4:将处理好的标签放入临时的map集合中
////                    for (String tag : set.keySet()) {
////                        //存放标签
////                        Map<String, Integer> map2 = map1.get(set.get(tag));
////                        if (map2 == null) {
////                            map2 = new HashMap<>(1);
////                            map2.put(tag, 1);
////                        } else {
////                            Integer integer = map2.get(tag);
////                            if (integer == null) {
////                                map2.put(tag, 1);
////                            } else {
////                                map2.put(tag, ++integer);
////                            }
////                        }
////                        map1.put(set.get(tag), map2);
////                    }
////                }
////                map.put(storeId, map1);
////            }
////        }
////        return map;
////    }
//
//    public List<Integer> sampleCommentSimple(String text, ESimnetType eSimnetType) {
//        //TODO  step 1:初始化百度AI请求参数
//        // 初始化一个AipNlp
//        AipNlp client = new AipNlp(baiDuAIProperties.getAppId(), baiDuAIProperties.getApiKey(), baiDuAIProperties.getSecretKey());
//
//        // 可选：设置网络连接参数
//        client.setConnectionTimeoutInMillis(2000);
//        client.setSocketTimeoutInMillis(60000);
//        // 传入可选参数调用接口
//        HashMap<String, Object> options = new HashMap<>();
//
//        if (text != null) {
//            //TODO  step 2.2:转化评论为标签，转化标签内容
//            JSONObject response = client.commentTag(text, eSimnetType, options);
//            if (response.has("ERROR_CODE")) {
//                return null;
//            }
//            JSONArray jsonArray = (JSONArray) response.get("items");
//            List list2 = jsonArray.toList();
//
//            //TODO  step 2.3:用一个集合排除一个用户一次评论里出现的重复标签
////            Set<Tag> tags = new HashSet<>();
////            for (int j = 0; j < list2.size(); j++) {
////                Map<String, Object> map2 = (Map<String, Object>) list2.get(j);
////                Tag tag = new Tag();
////                tag.setTag(map2.get("abstract").toString().replaceAll("</?span>", ""));
////                tag.setSentiment(Integer.parseInt(map2.get("sentiment").toString()));
////                tags.add(tag);
////            }
//            //保存tag
////            return tagService.saveOrUpdateTag(tags);
//        }
//        return null;
//    }
//
//    /**
//     * 百度AI之头像审核
//     *
//     * 适用于后端上传，图片是以二进制形式
//     */
////    public boolean sampleAvatar(String imagePath){
////        AipContentCensor client = new AipContentCensor(baiDuAIProperties.getAppId(), baiDuAIProperties.getApiKey(), baiDuAIProperties.getSecretKey());
////
////        // 可选：设置网络连接参数
////        client.setConnectionTimeoutInMillis(2000);
////        client.setSocketTimeoutInMillis(60000);
////
////        // 传入可选参数调用接口
////        HashMap<String, String> options = new HashMap<>();
////        // 参数为本地图片路径
//////        String imagePath = "static/img/logo.png";
////        List<String>list=new ArrayList<>();
////        list.add(imagePath);
////
////        byte[][] fileToByte=new byte[1][];
////            fileToByte[0]= getImageFromNetByUrl(imagePath);
////        JSONObject response = client.faceAudit(fileToByte, options);
////        if(response.has(ERROR_CODE)){
////            return false;
////        }
////        JSONArray result = (JSONArray) response.get("result");
////        List<Object> objects = result.toList();
////        for(Object o:objects){
////            Map<String,Integer> m=(Map<String,Integer>)o;
////            Integer res_code = m.get("res_code");
////            if(res_code==0){
////                return true;
////            }else{
////                return false;
////            }
////        }
////        return true;
////    }
////
//    /**
//     * 根据地址获得数据的字节流
//     * @param strUrl 网络连接地址
//     * @return
//     */
//    public static byte[] getImageFromNetByUrl(String strUrl){
////        try {
////            URL url = new URL(strUrl);
////            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
////            conn.setRequestMethod("GET");
////            conn.setConnectTimeout(5 * 1000);
////            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
////            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
////            return btImg;
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//        return null;
//    }
//
//    /**
//     * 图像审核接口，
//     *
//     * @param imagePath 图片URL多张URL以,分隔
//     * @return
//     */
//    public boolean sampleAvatar(String imagePath) {
//        if (imagePath == null) {
//            return true;
//        }
//        log.info(imagePath);
//        AipContentCensor client = new AipContentCensor(baiDuAIProperties.getAppId(), baiDuAIProperties.getApiKey(), baiDuAIProperties.getSecretKey());
//
//        // 参数为url
//        String[] split = imagePath.split(",");
//        for (String url : split) {
//            JSONObject response = client.imageCensorUserDefined(url, EImgType.URL, null);
////            Map map = JsonUtil.getObjectFromJson(response.toString(), Map.class);
////            String errorCode = (String) map.get("error_code");
////            if (errorCode != null) {
////                return false;
////            }
////            int conclusionType = (int) map.get("conclusionType");
////            if (conclusionType == FAIL) {
////                return false;
////            }
//        }
//        return true;
//    }
//
//    /**
//     * 图像审核接口，
//     *
//     * @param imageSet
//     * @return
//     */
//    public boolean sampleAvatar(Set<String> imageSet) {
//        if (imageSet == null) {
//            return true;
//        }
//        AipContentCensor client = new AipContentCensor(baiDuAIProperties.getAppId(), baiDuAIProperties.getApiKey(), baiDuAIProperties.getSecretKey());
//
//        // 参数为url
//        for (String imagePath : imageSet) {
//            JSONObject response = client.imageCensorUserDefined(imagePath, EImgType.URL, null);
////            Map map = JsonUtil.getObjectFromJson(response.toString(), Map.class);
////            Object errorCode = map.get("error_code");
////            if (errorCode != null) {
////                return false;
////            }
////            int conclusionType = (int) map.get("conclusionType");
////            if (conclusionType == FAIL) {
////                return false;
////            }
//        }
//        return true;
//    }
////    /**
////     * 头像审核接口，
////     *
////     * @param imagePath 图片URL多张URL以,分隔
////     * @return
////     */
////    public boolean sampleAvatar(String imagePath) {
////        if (imagePath == null) {
////            return true;
////        }
////        log.info(imagePath);
////        // 用户头像审核 url
////        String faceAuditUrl = "https://aip.baidubce.com/rest/2.0/solution/v1/face_audit";
////        // 请求参数
////        String configId = "1";
//////        String filePath = "#####本地文件路径#####";
////        try {
////            String params = "configId=" + configId + "&" + URLEncoder.encode("imgUrls", "UTF-8") + "=" + URLEncoder
////                    .encode(imagePath, "UTF-8");
////
//////            String accessToken = "24.00daa54af3eda6c86baf1e0d4f7db04d.2592000.1558183538.282335-15362917";
////            String accessToken = (String) cacheUtil.get(CacheKey.BAIDU_AI_TOKEN, "token");
////            if (accessToken == null) {
////                //重新获取token
////                accessToken = getAuth();
////                if (accessToken == null) {
////                    return false;
////                }
////                cacheUtil.set(CacheKey.BAIDU_AI_TOKEN, "token", accessToken, 29 * 24 * 60 * 60);
////                return sampleAvatar(imagePath);
////            }
////            String result = HttpUtil.post(faceAuditUrl, accessToken, params);
//////            System.out.println(result);
////            Map map = JsonUtil.getObjectFromJson(result, Map.class);
////            if (map.containsKey(ERROR_CODE)) {
////                Integer o = (Integer) map.get(ERROR_CODE);
////                if (o.equals(ERROR_CODE1) || o.equals(ERROR_CODE2)) {
////                    //重新获取token
////                    accessToken = getAuth();
////                    if (accessToken == null) {
////                        return false;
////                    }
////                    cacheUtil.set(CacheKey.BAIDU_AI_TOKEN, "token", accessToken, 29 * 24 * 60 * 60);
////                    return sampleAvatar(imagePath);
////                }
////                return false;
////            } else {
////                List result1 = (List) map.get("result");
////                for (int i = 0; i < result1.size(); i++) {
////                    Map result2 = (Map) result1.get(i);
////                    Map data = (Map) result2.get("data");
////                    if(data==null){
////                        return true;
////                    }
////                    if (data.get("antiporn") != null) {
////                        Map antiporn = (Map) data.get("antiporn");
////                        String conclusion = (String) antiporn.get("conclusion");
////                        if (!conclusion.equals(SUCC) && !conclusion.equals(SUCC2)) {
////                            return false;
////                        }
////                    }
////                }
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return true;
////    }
//
//    /**
//     * 获取权限token
//     *
//     * @return 返回示例：
//     * {
//     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
//     * "expires_in": 2592000
//     * }
//     */
//    public String getAuth() {
//        // 官网获取的 API Key 更新为你注册的
//        String clientId = baiDuAIProperties.getApiKey();
////        String clientId = "RsqFu633zHg043YB3hgreELD";
//        // 官网获取的 Secret Key 更新为你注册的
//        String clientSecret = baiDuAIProperties.getSecretKey();
////        String clientSecret = "MMt7Px9d9Id0LmSiKQz5ZXG6O7bLVsob ";
//        return getAuth(clientId, clientSecret);
//    }
//
//    /**
//     * 获取API访问token
//     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
//     *
//     * @param ak - 百度云官网获取的 API Key
//     * @param sk - 百度云官网获取的 Securet Key
//     * @return assess_token 示例：
//     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
//     */
//    public String getAuth(String ak, String sk) {
//        // 获取token地址
//        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
//        String getAccessTokenUrl = authHost
//                // 1. grant_type为固定参数
//                + "grant_type=client_credentials"
//                // 2. 官网获取的 API Key
//                + "&client_id=" + ak
//                // 3. 官网获取的 Secret Key
//                + "&client_secret=" + sk;
//        try {
//            URL realUrl = new URL(getAccessTokenUrl);
//            // 打开和URL之间的连接
//            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
//            connection.setRequestMethod("GET");
//            connection.connect();
//            // 获取所有响应头字段
//            Map<String, List<String>> map = connection.getHeaderFields();
//            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.err.println(key + "--->" + map.get(key));
//            }
//            // 定义 BufferedReader输入流来读取URL的响应
//            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String result = "";
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//            /**
//             * 返回结果示例
//             */
////            System.err.println("result:" + result);
//            JSONObject jsonObject = new JSONObject(result);
//            String access_token = jsonObject.getString("access_token");
//            return access_token;
//        } catch (Exception e) {
//            System.err.printf("获取token失败！");
//            e.printStackTrace(System.err);
//        }
//        return null;
//    }
//
//    /**
//     * 营业执照识别
//     * @param imagePath
//     * @return
//     * no   编号
//     * name 单位名称
//     */
//    public Map sample(String imagePath) {
//        // 初始化一个AipOcr
//        AipOcr client = new AipOcr(baiDuAIProperties.getAppId(), baiDuAIProperties.getApiKey(), baiDuAIProperties.getSecretKey());
//
//        // 可选：设置网络连接参数
//        client.setConnectionTimeoutInMillis(2000);
//        client.setSocketTimeoutInMillis(60000);
//
//        // 传入可选参数调用接口
//        HashMap<String, String> options = new HashMap<String, String>();
//
//
//        // 参数为本地路径
////        String image = "test.jpg";
////        JSONObject res = client.businessLicense(image, options);
////        System.out.println(res.toString(2));
//
//        // 参数为二进制数组
//        byte[] file = getImageFromNetByUrl(imagePath);
//        JSONObject res = client.businessLicense(file, options);
//        System.out.println(res.toString(2));
//        Map result=new HashMap(2);
//        try {
//            Map map = (Map)res.get("map");
//            Map wordsResult = (Map)map.get("words_result");
//            String no = (String)wordsResult.get("社会信用代码");
//            String name = (String)wordsResult.get("单位名称");
//            result.put("no",no);
//            result.put("name",name);
//        }catch (Exception n){
//            return null;
//        }
//
//        return result;
//    }
//
//    /**
//     * 身份证识别
//     * @param imagePath
//     */
//    public void idcard(String imagePath) {
//        // 初始化一个AipOcr
//        AipOcr client = new AipOcr(baiDuAIProperties.getAppId(), baiDuAIProperties.getApiKey(), baiDuAIProperties.getSecretKey());
//        // 传入可选参数调用接口
//        HashMap<String, String> options = new HashMap<String, String>();
//        options.put("detect_direction", "true");
//        options.put("detect_risk", "false");
//
//        //front：身份证含照片的一面；back：身份证带国徽的一面
//        String idCardSide = "back";
//
//        // 参数为二进制数组
//        JSONObject res = client.idcard(getImageFromNetByUrl(imagePath), idCardSide, options);
//        System.out.println(res.toString(2));
//    }
//
//    public static void main(String[] args) {
////        long l1=System.currentTimeMillis();
////        BaiDuAiUtil baiDuAiUtil = new BaiDuAiUtil();
////        baiDuAiUtil.sampleAvatar("http://47.104.86.42:4869/050b302210445713dfe51793fb02992e");
////        long l2=System.currentTimeMillis();
////        System.out.println("aaaaa+"+(l2-l1));
//
//
////        getAuth();
//    }

}
