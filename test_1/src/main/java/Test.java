import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.test.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class Test {

    public static void main(String[] args) {
        while(1==1) {
            System.out.print("(1.get 2.post-jsonbody 其他.post表单)\n请选择发送形式：");
            String a;
            Scanner sc = new Scanner(System.in);
            a = sc.next();
            if (a.equals("1")||a.equals("get")) {//有参get
                System.out.print("请输入参数:");
                String s;
                s = sc.next();
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpGet httpGet = new HttpGet("http://localhost:8080/gettest?canshu=" + s);
                CloseableHttpResponse response = null;
                //设置超时
                RequestConfig requestConfig = RequestConfig.custom()
                        // 设置连接超时时间(单位毫秒)
                        .setConnectTimeout(5000)
                        // 设置请求超时时间(单位毫秒)
                        .setConnectionRequestTimeout(5000)
                        // socket读写超时时间(单位毫秒)
                        .setSocketTimeout(5000)
                        // 设置是否允许重定向(默认为true)
                        .setRedirectsEnabled(true).build();

                // 将上面的配置信息 运用到这个Get请求里
                httpGet.setConfig(requestConfig);
                //
                try {
                    response = httpClient.execute(httpGet);
                    HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                       String r=EntityUtils.toString(responseEntity);
                       JSONObject j=JSONObject.parseObject(r);
                       P(r.length(), "*");
                       System.out.println("server(get路径)返回结果如下:");
                       if(Integer.parseInt(j.getString("code"))!=200){
                           System.out.println("参数不符合条件\n"+"code="+j.getString("code")+"\nmsg="+j.getString("msg"));
                       }
                       else{
                           System.out.println("code="+j.getString("code")+"\nmsg="+j.getString("msg")+"\ndata="+j.getString("data"));
                       }
                       Date d = new Date();
                       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss E");
                       System.out.println("\n返回时间 "+sdf.format(d));
                       P(r.length(), "*");
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        // 释放资源
                        if (httpClient != null) httpClient.close();
                        if (response != null) response.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (a.equals("2")||a.equals("post-jsonbody")) {
                Map<String,String> params=new HashMap<>();
                System.out.println("请输入对应json数据:");
                System.out.print("姓名:");String name=sc.next();
                params.put("姓名",name);
                System.out.print("性别:");String sex=sc.next();
                params.put("性别",sex);
                System.out.print("学号:");String id=sc.next();
                params.put("学号",id);
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost post = new HttpPost("http://localhost:8080/posttest1");
                //设置超时
                RequestConfig requestConfig = RequestConfig.custom()
                        // 设置连接超时时间(单位毫秒)
                        .setConnectTimeout(5000)
                        // 设置请求超时时间(单位毫秒)
                        .setConnectionRequestTimeout(5000)
                        // socket读写超时时间(单位毫秒)
                        .setSocketTimeout(5000)
                        // 设置是否允许重定向(默认为true)
                        .setRedirectsEnabled(true).build();

                // 将上面的配置信息 运用到这个Post请求里
                post.setConfig(requestConfig);
                post.setHeader("Content-type", "application/json; charset=utf8");
                CloseableHttpResponse response1=null;
                try{
                    if(params!=null){
                        StringEntity entity=new StringEntity(JSON.toJSONString(params),"UTF-8");
                        log.info("json字符串:"+JSON.toJSONString(params));
                        entity.setContentType("application/json");
                        post.setEntity(entity);
                    }
                    response1 = httpClient.execute(post);
                    log.info("已连接");
                    //接收springboot-post返回json
                    JSONObject jsonObject=new JSONObject();
                    if (response1.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                        HttpEntity entity=response1.getEntity();
                        String result=EntityUtils.toString(entity);
                        jsonObject=JSON.parseObject(result);
                        JSONObject data=jsonObject.getJSONObject("data");
                        Map<String,String> resultmap=JSONObject.toJavaObject(data,Map.class);
                        P(result.length()/2, "*");
                        System.out.println("server(post-json路径)返回结果如下:");
                        if(Integer.parseInt(jsonObject.getString("code"))!=200){
                            System.out.println("参数不符合条件\n"+"code="+jsonObject.getString("code")+"\nmsg="+jsonObject.getString("msg"));
                        }
                        else {
                            System.out.println("code=" + jsonObject.getString("code") + "\nmsg=" + jsonObject.getString("msg"));
                            System.out.println("返回姓名:" + resultmap.get("post_姓名"));
                            System.out.println("返回性别:" + resultmap.get("post_性别"));
                            System.out.println("返回学号:" + resultmap.get("post_学号"));
                        }
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss E");
                        System.out.println("\n返回时间 "+sdf.format(d));
                        P(result.length()/2, "*");
                    }
                }catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        // 释放资源
                        if (httpClient != null) httpClient.close();
                        if (response1 != null) response1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else//post表单
            {
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost("http://localhost:8080/posttest2");
                RequestConfig requestConfig = RequestConfig.custom()
                        // 设置连接超时时间(单位毫秒)
                        .setConnectTimeout(5000)
                        // 设置请求超时时间(单位毫秒)
                        .setConnectionRequestTimeout(5000)
                        // socket读写超时时间(单位毫秒)
                        .setSocketTimeout(5000)
                        // 设置是否允许重定向(默认为true)
                        .setRedirectsEnabled(true).build();
                // 将上面的配置信息 运用到这个Post请求里
                httpPost.setConfig(requestConfig);
                httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
                List<NameValuePair> params = new ArrayList<>();
                System.out.println("请输入参数:");
                System.out.print("姓名:");String name=sc.next();
                params.add(new BasicNameValuePair("name",name));
                System.out.print("性别:");String sex=sc.next();
                params.add(new BasicNameValuePair("sex",sex));
                System.out.print("学号:");String id=sc.next();
                params.add(new BasicNameValuePair("studentId",id));
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params,StandardCharsets.UTF_8);
                httpPost.setEntity(formEntity);
                CloseableHttpResponse response2 = null;
                try {
                    response2=httpClient.execute(httpPost);
                    HttpEntity responseEntity=response2.getEntity();
                    if(responseEntity!=null){
//                      System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
                        String r=EntityUtils.toString(responseEntity);
                        JSONObject j=JSONObject.parseObject(r);
                        P(r.length(), "*");
                        System.out.println("server(post-表单路径)返回结果如下:");
                        if(Integer.parseInt(j.getString("code"))!=200)
                            System.out.println("参数不符合条件\n"+"code="+j.getString("code")+"\nmsg="+j.getString("msg"));
                        else
                            System.out.println("code=" + j.getString("code") + "\nmsg=" + j.getString("msg") + "\ndata=" + j.getString("data"));
                        Date d = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss E");
                        System.out.println("\n返回时间:"+sdf.format(d));
                        P(r.length(), "*");
                    }
                }catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        // 释放资源
                        if (httpClient != null) {
                            httpClient.close();
                        }
                        if (response2 != null) {
                            response2.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("\n\n\n");
        }
    }

    public static void P(Long num, String s) {
        for (int i = 0; i < num*2; i++) {
            System.out.print(s);
        }
        System.out.println("");
    }

    public static void P(int num, String s) {
        for (int i = 0; i < num*2; i++) {
            System.out.print(s);
        }
        System.out.println("");
    }
}
