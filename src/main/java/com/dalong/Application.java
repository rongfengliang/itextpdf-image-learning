package com.dalong;

import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 @author dalong
*/
public class Application {
    public static void export(){
        try {
            String fileName = "src/main/resources/授课时间-image.pdf";
            String imageFile = "src/main/resources/zeebe.png";
            PdfReader reader = new PdfReader(fileName);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfStamper ps = new PdfStamper(reader, bos);
            // 中文字体问题
            BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            ArrayList<BaseFont> fontList = new ArrayList<BaseFont>();
            fontList.add(bf);
            AcroFields fields = ps.getAcroFields();
            fields.setSubstitutionFonts(fontList);
            fillData(fields, data());
            // 添加图片
            addImage(ps,fields,"logo",imageFile);
            ps.setFormFlattening(true);
            ps.close();
            //生成pdf路径存放的路径
            OutputStream fos = new FileOutputStream("result.pdf");
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
            bos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 填充模板中的数据
     */
    public static void fillData(AcroFields fields, Map<String, String> data) {
        try {
            for (String key : data.keySet()) {
                String value = data.get(key);
                fields.setField(key, value);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void addImage(PdfStamper stamper,AcroFields form,String field,String fieldValue){
        try{
            java.util.List<AcroFields.FieldPosition> photograph = form.getFieldPositions(field);
            if(photograph!=null && photograph.size()>0){
                Rectangle rect= photograph.get(0).position;
                Image img = Image.getInstance(fieldValue);
                img.scaleToFit(rect.getWidth(), rect.getHeight());
                img.setBorder(2);
                img.setAbsolutePosition(
                        photograph.get(0).position.getLeft() + (rect.getWidth() - img.getScaledWidth() )
                        , photograph.get(0).position.getTop() - (rect.getHeight()));
                PdfContentByte cb = stamper.getOverContent((int)photograph.get(0).page);
                cb.addImage(img);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 填充数据源
     * 其中data存放的key值与pdf模板中的文本域值相对应
     */
    public static Map<String, String> data() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("year", "2020");
        data.put("marks","摘要：这个本来属于s3 的特性，但是我们在实际使用的过程中肯定不想别人直接可以通过浏览器或者http就可以可以我们的文件内容 这个属于安全的控制，以下是一个实践以及一些安全控制 一些原则 不能直接暴露minio 访问到公网环境（可以基于nginx，以及反向代理工具解决） 配置合理的bucket 策略，可以 阅读全文\n");
        data.put("d","因为当前大家主流的还是基于前后端分离的模式开发软件，组件+api 实现功能，但是很多时候好多租户对于功能有个性化需求，但是\n" +
                "系统在设计的时候因为时间问题+早期设计问题造成业务扩展能力有点差，还需要支持个性化需求开发，所以我们可以拆分标准版本\n" +
                "以及自定型版本，同时基于minio 提供的s3 管理模式，对于不同的租户创建不同的bucket，标准的使用标准版，这样客户化开发就很简单\n" +
                "了（特殊场景需要个性化），此方案的缺点也比较明确：空间的占用，但是还好因为前后端分离的模式。每个租户占用的静态资源也不是\n" +
                "很大，核心问题是在系统更新的时候，我们可能需要引导客户自己升级或者基于强大的ci/cd 系统进行所有租户的系统升级（构建包的处理）\n" +
                "个人感觉好处也是很明显的，如果我们的api 以及website 已经做了比较好的版本管理，用户切换版本也就是静态资源的替换（直接基于s3 bucket）。\n" +
                "saas 应用的功能主要基于单页面的模式开发的应用，在s3 中的存储就是css，js 以及静态html 页面。这样管理起来也是比较灵活的");
        return data;
    }
    public static void main(String[] args) {
        export();
    }
}
