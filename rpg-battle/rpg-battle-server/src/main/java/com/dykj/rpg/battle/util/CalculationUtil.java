package com.dykj.rpg.battle.util;

import com.dykj.rpg.util.random.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class CalculationUtil {

    /**
     * 由方向向量返回角度值（0~360）
     */
    public static float vectorToAngle(float x,float y){
        if(x==0){
            if(y>0){
                return 90;
            }
            if(y==0){
                return 0;
            }
            if(y<0){
                return 270;
            }
        }
        float a = (float)(180*Math.atan(Math.abs(y/x))/Math.PI);
        //第一象限
        if(x>0 && y>0){
            return a;
        }
        //第二象限
        if(x<0 && y>0){
            return 180-a;
        }
        //第三象限
        if(x<0 && y<0){
            return 180+a;
        }
        //第四象限
        if(x>0 && y<0){
            return 360-a;
        }
        return 0;
    }

    /**
     * 判断点是否在圆形范围内
     */
    public static boolean isPointInCircle(float radius,float[] point){
        if(Math.sqrt(Math.pow(point[0],2)+Math.pow(point[1],2)) < radius){
            return true;
        }
        return false;
    }

    /**
     * 判断点是否在扇形(环形)范围内,以dir方向两边各angle/2角度的范围 距离
     */
    public static boolean isPointInSector(float minRadius,float maxRadius,float angle,float[] dir,float[] point){
        float distance = (float)Math.sqrt(Math.pow(point[0],2)+Math.pow(point[1],2));
        if(distance > minRadius && distance < maxRadius){

            float dirAngle = vectorToAngle(dir[0],dir[1]);

            float pointAngle = vectorToAngle(point[0],point[1]);

            if(pointAngle > dirAngle-angle/2 && pointAngle < dirAngle+angle/2){
                return true;
            }

            if(pointAngle - 360 > dirAngle-angle/2 && pointAngle - 360 < dirAngle+angle/2){
                return true;
            }

            if(pointAngle + 360 > dirAngle-angle/2 && pointAngle + 360 < dirAngle+angle/2){
                return true;
            }

            return false;
        }
        return false;
    }

    /**                                 length
     *                    -------------------------------
     *                    |                             |
     * 判断点是否在矩形范围内 起点                           |  width
     *                    |                             |
     *                    -------------------------------
     */
    public static boolean isPointInRectangle(float width,float length,float[] dir,float[] point){
        /**
         * 将点的坐标转换为以矩形长为X，宽为Y的坐标系内的坐标
         * 坐标轴旋转公式 x′=x∗cosθ+y∗sinθ ，y′=y∗cosθ−x∗sinθ
         */
        double a = vectorToAngle(dir[0],dir[1])*Math.PI/180;
        double changeX = point[0]*Math.cos(a)+point[1]*Math.sin(a);
        double changeY = point[1]*Math.cos(a)-point[0]*Math.sin(a);
        if(changeX < length && changeX > 0 && changeY < width/2 && changeY > -width/2){
            return true;
        }
        return false;
    }

    /**                                 length
     *                    -------------------------------
     *                    |                             |
     * 判断点是否在矩形范围内 起点                           |  width
     *                    |                             |
     *                    -------------------------------
     */
    public static boolean isPointInRectangle(float width,float length,short dir,float[] point){
        /**
         * 将点的坐标转换为以矩形长为X，宽为Y的坐标系内的坐标
         * 坐标轴旋转公式 x′=x∗cosθ+y∗sinθ ，y′=y∗cosθ−x∗sinθ
         */
        double a = dir*Math.PI/180;
        double changeX = point[0]*Math.cos(a)+point[1]*Math.sin(a);
        double changeY = point[1]*Math.cos(a)-point[0]*Math.sin(a);
        if(changeX < length && changeX > 0 && changeY < width/2 && changeY > -width/2){
            return true;
        }
        return false;
    }

    /**
     * @param r         半径
     * @param angleMax  偏移最大角度
     * @param angleMin  偏移角度
     * @param playerPos 玩家的坐标
     * @param targetPos 目标的坐标
     * @return
     */
    public static float[] randomSoulPointByTarget(float r, float angleMax, float angleMin, float[] playerPos, float[] targetPos) {
        List<float[]> resultArray = new ArrayList<>();
        float changeX = playerPos[0] - targetPos[0];
        float changeZ = playerPos[2] - targetPos[2];
        float anglePT = vectorToAngle(changeX, changeZ);
        //算出相对于targetPos playerPos的角度
        float beginAg1 = anglePT - angleMax / 2;
        float endAg1 = anglePT - angleMin / 2;
        float agRs1 = (float) Math.random() * (endAg1 - beginAg1) + beginAg1;
        float point1X = (float) Math.cos(agRs1 * Math.PI / 180) * r + targetPos[0];
        float point1Z = (float) Math.sin(agRs1 * Math.PI / 180) * r + targetPos[2];
        resultArray.add(new float[]{point1X, 0, point1Z});
        //算出相对于targetPos playerPos的角度
        float beginAg2 = anglePT + angleMin / 2;
        float endAg2 = anglePT + angleMax / 2;
        float agRs2 = (float) Math.random() * (endAg2 - beginAg2) + beginAg2;
        float point2X = (float) Math.cos(agRs2 * Math.PI / 180) * r + targetPos[0];
        float point2Z = (float) Math.sin(agRs2 * Math.PI / 180) * r + targetPos[2];
        resultArray.add(new float[]{point2X, 0, point2Z});
        return resultArray.get(RandomUtil.randomBetween(0, resultArray.size()));

    }


    public static float[] randomSoulPointByPlayer(float r, float angle, float[] playerPos, float[] targetPos) {
        List<float[]> resultArray = new ArrayList<>();
        float changeX = targetPos[0] - playerPos[0];
        float changeZ = targetPos[0] - playerPos[2];
        //算出相对于targetPos playerPos的角度
        float anglePT = vectorToAngle(changeX, changeZ);
        float beginAg1 = 0;
        float endAg1 = anglePT - angle / 2;
        float agRs1 = (float) Math.random() * (endAg1 - beginAg1) + beginAg1;
        float point1X = (float) Math.cos(agRs1 * Math.PI / 180) * r + targetPos[0];
        float point1Z = (float) Math.sin(agRs1 * Math.PI / 180) * r + targetPos[2];
        resultArray.add(new float[]{point1X, 0, point1Z});

        //算出相对于targetPos playerPos的角度
        float beginAg2 = anglePT + angle / 2;
        float endAg2 = 360;
        float agRs2 = (float) Math.random() * (endAg2 - beginAg2) + beginAg2;
        float point2X = (float) Math.cos(agRs2 * Math.PI / 180) * r + targetPos[0];
        float point2Z = (float) Math.sin(agRs2 * Math.PI / 180) * r + targetPos[2];
        resultArray.add(new float[]{point2X, 0, point2Z});
        return resultArray.get(RandomUtil.randomBetween(0, resultArray.size()));

    }
}
