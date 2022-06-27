package antihackerman.util;

import antihackerman.messaging.Filter;
import antihackerman.messaging.FilterType;
import antihackerman.model.DeviceType;
import org.json.simple.JSONObject;

import java.util.Locale;
import java.util.regex.Pattern;

public class FilterUtil {

    public static Filter createFilter(String filterStr){
        Filter filter = new Filter();
        filterStr = filterStr.trim().toLowerCase();
        if (isTrue(filterStr)){
            filter.setFilterType(FilterType.TRUE);
            filter.setBoolValue(true);
        }else if (isFalse(filterStr)){
            filter.setFilterType(FilterType.FALSE);
            filter.setBoolValue(false);
        }else if (isEqual(filterStr)){
            filter.setFilterType(FilterType.EQUAL);
            filter.setValue(Double.parseDouble(filterStr.substring(1)));
        }else if (isLess(filterStr)){
            filter.setFilterType(FilterType.LESS);
            filter.setValue(Double.parseDouble(filterStr.substring(1)));
        }else if (isGreater(filterStr)){
            filter.setFilterType(FilterType.GREATER);
            filter.setValue(Double.parseDouble(filterStr.substring(1)));
        }else{
            return null;
        }

        return filter;
    }

    public static boolean isTrue(String filterStr){
        if (filterStr.contentEquals("true")){
            return true;
        }
        return false;
    }

    public static boolean isFalse(String filterStr){
        if (filterStr.contentEquals("false")){
            return true;
        }
        return false;
    }

    public static boolean isEqual(String filterStr){
        String regexPattern = "^=[1-9][0-9]*$";
        if (Pattern.compile(regexPattern).matcher(filterStr).matches()){
            return true;
        }
        return false;
    }

    public static boolean isLess(String filterStr){
        String regexPattern = "^<[1-9][0-9]*$";
        if (Pattern.compile(regexPattern).matcher(filterStr).matches()){
            return true;
        }
        return false;
    }

    public static boolean isGreater(String filterStr){
        String regexPattern = "^>[1-9][0-9]*$";
        if (Pattern.compile(regexPattern).matcher(filterStr).matches()){
            return true;
        }
        return false;
    }

    public static boolean passedFilter(Filter filter, JSONObject msg, DeviceType deviceType){
        if (deviceType.equals(DeviceType.BOOLEAN_DEVICE)){
            if (filter.getFilterType().equals(FilterType.TRUE) && (Long)msg.get("value")==1){
                return true;
            }else if (filter.getFilterType().equals(FilterType.FALSE) && (Long)msg.get("value")==0){
                return true;
            }
            return false;
        }else if (deviceType.equals(DeviceType.INTERVAL_DEVICE)){
            if (filter.getFilterType().equals(FilterType.EQUAL) && (Long)msg.get("value")==filter.getValue()){
                return true;
            }else if (filter.getFilterType().equals(FilterType.LESS) && (Long)msg.get("value")<filter.getValue()){
                return true;
            }else if (filter.getFilterType().equals(FilterType.GREATER) && (Long)msg.get("value")>filter.getValue()){
                return true;
            }
            return false;
        }
        return false;
    }
}
