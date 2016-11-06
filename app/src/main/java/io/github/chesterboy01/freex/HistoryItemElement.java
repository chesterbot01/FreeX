package io.github.chesterboy01.freex;

/**
 * Created by Administrator on 10/30/2016.
 */

public class HistoryItemElement {

    // private HttpServletRequest request;
    //private HttpServletResponse response;


    //服务器端的通信
    //http://blog.csdn.net/woshisap/article/details/6621571
    /*public void json() {
        JSONArray jsonArray = new JSONArray();

        JSONObject jsonObject1 = new JSONObject();
        jsonObject.put("intype", CurrencyType.CAD);
        jsonObject.put("inamount", 100);
        jsonObject.put("outtype", CurrencyType.RMB);
        jsonObject.put("outamount", 505);

        JSONObject jsonObject2 = new JSONObject();
        jsonObject.put("intype", CurrencyType.RMB);
        jsonObject.put("inamount", 451);
        jsonObject.put("outtype", CurrencyType.USD);
        jsonObject.put("outamount", 66);

        JSONObject jsonObject3 = new JSONObject();
        jsonObject.put("intype", CurrencyType.USD);
        jsonObject.put("inamount", 243);
        jsonObject.put("outtype", CurrencyType.CAD);
        jsonObject.put("outamount", 170);

        jsonArray.add(0, jsonObject);
        jsonArray.add(1, jsonObject1);
        jsonArray.add(2, jsonObject2);

        try {
            this.response.setCharacterEncoding("UTF-8");
            this.response.getWriter().write(jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


    /*public void receiveFromServer() {
        List<HashMap<String, Object>> videos = null;
        HashMap<String, Object> video = null;
        ListView listView = null;
        String url = "http://10.0.2.2:8088/Struts2_sxt/getjson.action";

        HttpClient client = new DefaultHttpClient();
        //提拱默认的HttpClient实现
        HttpPost request;
        try {
            request = new HttpPost(new URI(url));
            HttpResponse response = client.execute(request);
            // 判断请求是否成功
            if (response.getStatusLine().getStatusCode() == 200) { //200表示请求成功
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String out = EntityUtils.toString(entity, "UTF-8");
                    Log.i(TAG, out);

                    JSONArray jsonArray = new JSONArray(out);

                    videos = new ArrayList<HashMap<String, Object>>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("title");
                        int timelength = jsonObject.getInt("timelength");

                        video = new HashMap<String, Object>();
                        video.put("id", id);
                        video.put("name", name);
                        video.put("timelength", "时长为:" + timelength);

                        videos.add(video);
                    }

                    SimpleAdapter adapter = new SimpleAdapter(c, videos, R.layout.history_list_item,
                            new String[]{"name", "timelength"},
                            new int[]{R.id.outtype, R.id.outamount}
                    );
                    listView.setAdapter(adapter);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
            //Toast.makeText(FragHistory.this, "获取数据失败", Toast.LENGTH_LONG).show();
        }
    }*/

}



















