package manhua.easou.com.mydanmutest.utils;

import android.text.TextUtils;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Log的工具类，可以显示出具体打log的地方，在AS的Log日志中点击跳转。
 * 用printStackTraceLine 将日志框起来排版。
 * 根据数据类型打印普通log，json，xml类型日志
 * Created by regan.chon on 2017/4/14.
 */
public class AppLog {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final String NULL_TIPS = "Log with null object";

    private static final String DEFAULT_MESSAGE = "execute";
    private static final String PARAM = "Param";
    private static final String NULL = "null";
    private static final String TAG_DEFAULT = "easou_tag";
    private static final String SUFFIX = ".java";

    public static final int JSON_INDENT = 4;
    public static final int V = 0x1;

    public static final int D = 0x2;
    public static final int I = 0x3;
    public static final int W = 0x4;
    public static final int E = 0x5;
    public static final int A = 0x6;

    private static final int JSON = 0x7;
    private static final int XML = 0x8;

    private static final int STACK_TRACE_INDEX = 5;

    private static String mGlobalTag = "easou_tag";
    private static boolean mIsGlobalTagEmpty = true;
    private static boolean IS_SHOW_LOG = true;

//    public static void init(boolean isShowLog) {
//        IS_SHOW_LOG = isShowLog;
//    }
//
//    public static void init(boolean isShowLog, @Nullable String tag) {
//        IS_SHOW_LOG = isShowLog;
//        mGlobalTag = tag;
//        mIsGlobalTagEmpty = TextUtils.isEmpty(mGlobalTag);
//    }

    public static void printStackTrace() {
        if (IS_SHOW_LOG) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            int length = stackTrace.length;
            printStackTraceLine(mGlobalTag, true);

            Log.i(mGlobalTag, "║   " + "current thread name : " + Thread.currentThread().getName());
            //ignore top 3 traces.
            for (int i = 3; i < length; i++) {
                StackTraceElement targetElement = stackTrace[i];
                String className = targetElement.getClassName();
                String[] classNameInfo = className.split("\\.");
                if (classNameInfo.length > 0) {
                    className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
                }

                if (className.contains("$")) {
                    className = className.split("\\$")[0] + SUFFIX;
                }

                String methodName = targetElement.getMethodName();
                int lineNumber = targetElement.getLineNumber();

                if (lineNumber < 0) {
                    lineNumber = 0;
                }

                String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                String headString = "║ " + "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] " + " <--- "; //works


                Log.i(mGlobalTag, headString);
            }
            printStackTraceLine(mGlobalTag, false);
        }
    }


    public static void printHashMapObj(Object map) {
        if (IS_SHOW_LOG) {
            printHashMapLine(mGlobalTag, true);

            String str = map.toString();
            String subStr = str.substring(1, str.length() - 1);
            String[] objList = subStr.split(",");
            Log.i(mGlobalTag, "║ " + "total element number : " + objList.length);
            for (String obj : objList) {
                String[] kv = obj.split("=");
                Log.i(mGlobalTag, "║ " + "key = " + kv[0].trim() + ", value = " + kv[1].trim());
            }
            printHashMapLine(mGlobalTag, false);
        }
    }

    public static void printArrayListObj(Object list) {
        if (IS_SHOW_LOG) {
            printArrayListLine(mGlobalTag, true);

            ArrayList<Object> objs = (ArrayList<Object>) list;
            Log.i(mGlobalTag, "║ " + "total element number : " + objs.size());
            for (int i = 0; i < objs.size(); i++) {
                String str = objs.get(i).toString();
                Log.i(mGlobalTag, "║ " + "item = " + str);
            }

            printArrayListLine(mGlobalTag, false);
        }
    }

    public static void v() {
        printLog(V, null, DEFAULT_MESSAGE);
    }

    public static void v(Object msg) {
        printLog(V, null, msg);
    }

    public static void v(String tag, Object... objects) {
        printLog(V, tag, objects);
    }

    public static void d() {
        printLog(D, null, DEFAULT_MESSAGE);
    }

    public static void d(Object msg) {
        printLog(D, null, msg);
    }

    public static void d(String tag, Object... objects) {
        printLog(D, tag, objects);
    }

    public static void i() {
        printLog(I, null, DEFAULT_MESSAGE);
    }

    public static void i(Object msg) {
        printLog(I, null, msg);
    }

    public static void i(String tag, Object... objects) {
        printLog(I, tag, objects);
    }

    public static void w() {
        printLog(W, null, DEFAULT_MESSAGE);
    }

    public static void w(Object msg) {
        printLog(W, null, msg);
    }

    public static void w(String tag, Object... objects) {
        printLog(W, tag, objects);
    }

    public static void e() {
        printLog(E, null, DEFAULT_MESSAGE);
    }

    public static void e(Object msg) {
        printLog(E, null, msg);
    }

    public static void e(String tag, Object... objects) {
        printLog(E, tag, objects);
    }

    public static void a() {
        printLog(A, null, DEFAULT_MESSAGE);
    }

    public static void a(Object msg) {
        printLog(A, null, msg);
    }

    public static void a(String tag, Object... objects) {
        printLog(A, tag, objects);
    }

    public static void json(String jsonFormat) {
        printLog(JSON, null, jsonFormat);
    }

    public static void json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }

    public static void xml(String xml) {
        printLog(XML, null, xml);
    }

    public static void xml(String tag, String xml) {
        printLog(XML, tag, xml);
    }

    private static void printLog(int type, String tagStr, Object... objects) {
        if (!IS_SHOW_LOG) {
            return;
        }

        String[] contents = wrapperContent(tagStr, objects);
        String tag = contents[0];
        String msg = contents[1];
        String headString = contents[2];

        switch (type) {
            case V:
            case D:
            case I:
            case W:
            case E:
            case A:
                printDefault(type, tag, headString + msg);
                break;
            case JSON:
                printJson(tag, msg, headString);
                break;
            case XML:
                printXml(tag, msg, headString);
                break;
        }
    }

    private static String[] wrapperContent(String tagStr, Object... objects) {

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement targetElement = stackTrace[STACK_TRACE_INDEX];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
        }

        if (className.contains("$")) {
            className = className.split("\\$")[0] + SUFFIX;
        }

        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();

        if (lineNumber < 0) {
            lineNumber = 0;
        }

        String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

//        String tag = (tagStr == null ? className : tagStr);
        String tag = (tagStr == null ? "easou_tag" : tagStr);

        if (mIsGlobalTagEmpty && TextUtils.isEmpty(tag)) {
            tag = TAG_DEFAULT;
        } else if (!mIsGlobalTagEmpty) {
            tag = mGlobalTag;
        }

        String msg = (objects == null) ? NULL_TIPS : getObjectsString(objects);
        String headString = "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] ";

        return new String[]{tag, msg, headString};
    }

    private static String getObjectsString(Object... objects) {

        if (objects.length > 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("\n");
            for (int i = 0; i < objects.length; i++) {
                Object object = objects[i];
                if (object == null) {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(NULL).append("\n");
                } else {
                    stringBuilder.append(PARAM).append("[").append(i).append("]").append(" = ").append(object.toString()).append("\n");
                }
            }
            return stringBuilder.toString();
        } else {
            Object object = objects[0];
            return object == null ? NULL : object.toString();
        }
    }

    public static void printDefault(int type, String tag, String msg) {
        if (IS_SHOW_LOG) {
            int index = 0;
            int maxLength = 4000;
            int countOfSub = msg.length() / maxLength;

            if (countOfSub > 0) {
                for (int i = 0; i < countOfSub; i++) {
                    String sub = msg.substring(index, index + maxLength);
                    printSub(type, tag, sub);
                    index += maxLength;
                }
                printSub(type, tag, msg.substring(index, msg.length()));
            } else {
                printSub(type, tag, msg);
            }
        }
    }

    private static void printSub(int type, String tag, String sub) {
        switch (type) {
            case AppLog.V:
                Log.v(tag, sub);
                break;
            case AppLog.D:
                Log.d(tag, sub);
                break;
            case AppLog.I:
                Log.i(tag, sub);
                break;
            case AppLog.W:
                Log.w(tag, sub);
                break;
            case AppLog.E:
                Log.e(tag, sub);
                break;
            case AppLog.A:
                Log.wtf(tag, sub);
                break;
        }
    }

    private static void printStackTraceLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔════════════════════════════════ Stack Trace Begin ══════════════════════════════════");
        } else {
            Log.d(tag, "╚════════════════════════════════ Stack Trace End ════════════════════════════════════");
        }
    }

    private static void printHashMapLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔════════════════════════════════ HashMap object Begin ══════════════════════════════════");
        } else {
            Log.d(tag, "╚════════════════════════════════ HashMap object End ════════════════════════════════════");
        }
    }

    private static void printArrayListLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔════════════════════════════════ ArrayList object Begin ══════════════════════════════════");
        } else {
            Log.d(tag, "╚════════════════════════════════ ArrayList object End ════════════════════════════════════");
        }
    }

    public static void printJson(String tag, String msg, String headString) {
        if (IS_SHOW_LOG) {
            String message;

            try {
                if (msg.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(msg);
                    message = jsonObject.toString(AppLog.JSON_INDENT);
                } else if (msg.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(msg);
                    message = jsonArray.toString(AppLog.JSON_INDENT);
                } else {
                    message = msg;
                }
            } catch (JSONException e) {
                message = msg;
            }

            printJsonLine(tag, true);
            message = headString + AppLog.LINE_SEPARATOR + message;
            String[] lines = message.split(AppLog.LINE_SEPARATOR);
            for (String line : lines) {
                Log.d(tag, "║ " + line);
            }
            printJsonLine(tag, false);
        }
    }

    public static void printJsonLine(String tag, boolean isTop) {
        if (IS_SHOW_LOG) {
            if (isTop) {
                Log.d(tag, "╔═══════════════════════════ Json Begin ════════════════════════════════════════════════");
            } else {
                Log.d(tag, "╚═══════════════════════════ Json End ══════════════════════════════════════════════════");
            }
        }
    }

    public static void printXml(String tag, String xml, String headString) {
        if (IS_SHOW_LOG) {
            if (xml != null) {
                xml = formatXML(xml);
                xml = headString + "\n" + xml;
            } else {
                xml = headString + AppLog.NULL_TIPS;
            }

            printXmlLine(tag, true);
            String[] lines = xml.split(AppLog.LINE_SEPARATOR);
            for (String line : lines) {
                if (!isEmpty(line)) {
                    Log.d(tag, "║ " + line);
                }
            }
            printXmlLine(tag, false);
        }
    }

    public static String formatXML(String inputXML) {
        if (IS_SHOW_LOG) {
            try {
                Source xmlInput = new StreamSource(new StringReader(inputXML));
                StreamResult xmlOutput = new StreamResult(new StringWriter());
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(xmlInput, xmlOutput);
                return xmlOutput.getWriter().toString().replaceFirst(">", ">\n");
            } catch (Exception e) {
                e.printStackTrace();
                return inputXML;
            }
        }
        return null;
    }

    public static void printXmlLine(String tag, boolean isTop) {
        if (IS_SHOW_LOG) {
            if (isTop) {
                Log.d(tag, "╔═════════════════════════════ XML Begin ═══════════════════════════════════════════════");
            } else {
                Log.d(tag, "╚═════════════════════════════ XML End ═════════════════════════════════════════════════");
            }
        }
    }

    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }


    public static void printStackTraceError() {
        if (IS_SHOW_LOG) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            int length = stackTrace.length;
            printStackTraceLineError(mGlobalTag, true);

            Log.e(mGlobalTag, "║   " + "current thread name : " + Thread.currentThread().getName());
            //ignore top 3 traces.
            for (int i = 3; i < length; i++) {
                StackTraceElement targetElement = stackTrace[i];
                String className = targetElement.getClassName();
                String[] classNameInfo = className.split("\\.");
                if (classNameInfo.length > 0) {
                    className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
                }

                if (className.contains("$")) {
                    className = className.split("\\$")[0] + SUFFIX;
                }

                String methodName = targetElement.getMethodName();
                int lineNumber = targetElement.getLineNumber();

                if (lineNumber < 0) {
                    lineNumber = 0;
                }

                String methodNameShort = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                String headString = "║ " + "[ (" + className + ":" + lineNumber + ")#" + methodNameShort + " ] " + " <--- "; //works


                Log.e(mGlobalTag, headString);
            }
            printStackTraceLineError(mGlobalTag, false);
        }
    }

    private static void printStackTraceLineError(String tag, boolean isTop) {
        if (isTop) {
            Log.e(tag, "╔════════════════════════════════ Stack Trace Begin ══════════════════════════════════");
        } else {
            Log.e(tag, "╚════════════════════════════════ Stack Trace End ════════════════════════════════════");
        }
    }

}