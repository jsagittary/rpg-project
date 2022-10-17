package com.dykj.rpg.common.attribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jyb
 * @date 2020/12/2 16:27
 * @Description
 */
public class AttributeUtil {


    private static Logger logger = LoggerFactory.getLogger(AttributeUtil.class);

    /**
     * 1:1:2:3;1:1:2:3
     *
     * @param attributeStr
     * @return
     */
    public static List<AttributeUnit> coverToAttributeUnits(String attributeStr) {
        String[] rs = attributeStr.split("\\;");
        List<AttributeUnit> attributeUnits = new ArrayList<>();
        for (String attribute : rs) {
            String[] _attribute = attribute.split(":");
            if (_attribute.length != 4) {
                logger.error("attributeUtil coverToAttributeUnits error {} ", attributeStr);
                return attributeUnits;
            }
            attributeUnits.add(new AttributeUnit(Integer.valueOf(_attribute[0]), Integer.valueOf(_attribute[1]), Integer.valueOf(_attribute[2]), Integer.valueOf(_attribute[3])));
        }
        return attributeUnits;
    }


    /**
     * 1:1:2:3;1:1:2:3
     *
     * @param attributeStr
     * @return
     */
    public static List<AttributeUnit> coverToAttributeUnits(List<String> attributeStr) {
        List<AttributeUnit> attributeUnits = new ArrayList<>();
        for (String attribute : attributeStr) {
            String[] _attribute = attribute.split(":");
            if (_attribute.length != 4) {
                logger.error("attributeUtil coverToAttributeUnits error {} ", attributeStr);
                return attributeUnits;
            }
            attributeUnits.add(new AttributeUnit(Integer.valueOf(_attribute[0]), Integer.valueOf(_attribute[1]), Integer.valueOf(_attribute[2]), Integer.valueOf(_attribute[3])));
        }
        return attributeUnits;
    }





    /**
     * 1:1:2:3;1:1:2:3
     *
     * @param attributeStr
     * @return
     */
    public static List<List<Integer>> coverToList(String attributeStr) {
        String[] rs = attributeStr.split("\\;");
        List<List<Integer>> r = new ArrayList<>();
        for (String attribute : rs) {
            String[] _attribute = attribute.split(":");
            if (_attribute.length != 4) {
                logger.error("attributeUtil coverToList error {} ", attributeStr);
                return null;
            }
            List<Integer> _rs = new ArrayList<>();
            _rs.add(Integer.valueOf(_attribute[0]));
            _rs.add(Integer.valueOf(_attribute[1]));
            _rs.add(Integer.valueOf(_attribute[2]));
            _rs.add(Integer.valueOf(_attribute[3]));
            r.add(_rs);
        }
        return r;
    }


    /**
     * 1:1:2:3;1:1:2:3
     *
     * @param attributeStr
     * @return
     */
    public static List<List<Integer>> coverToList(List<String> attributeStr) {
        List<List<Integer>> r = new ArrayList<>(attributeStr.size());
        for (String attribute : attributeStr) {
            String[] _attribute = attribute.split(":");
            if (_attribute.length != 4) {
                logger.error("attributeUtil coverToList error {} ", attributeStr);
                return null;
            }
            List<Integer> _rs = new ArrayList<>();
            _rs.add(Integer.valueOf(_attribute[0]));
            _rs.add(Integer.valueOf(_attribute[1]));
            _rs.add(Integer.valueOf(_attribute[2]));
            _rs.add(Integer.valueOf(_attribute[3]));
            r.add(_rs);
        }
        return r;
    }

    /**
     * 合并多个List<AttributeUnit>
     *
     * @param lists
     * @return
     */
    public static Map<String, Integer> sumAttributeList(List<AttributeUnit>... lists) {
        Map<String, Integer> result = new HashMap<>();
        for (List<AttributeUnit> _list : lists) {
            for (AttributeUnit attributeUnit : _list) {
                Integer value = result.get(attributeUnit.key());
                result.put(attributeUnit.key(), value == null ? attributeUnit.getValue() : value + attributeUnit.getValue());
            }
        }
        return result;
    }
}