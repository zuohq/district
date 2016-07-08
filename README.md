# district

![image](https://github.com/zuohq/district/blob/master/2016-07-08_15_53_19.gif)

#Usage

一. 省-市-区 数据库创建及表结构设计
    daoexamplegenerator 
    
    public static void main(String[] args) throws Exception {
    
            Schema schema = new Schema(1, "com.martin.district.db");
    
            addDistrict(schema);
            new DaoGenerator().generateAll(schema, "app/src/main/java");
        }
        
二. 省-市-区数据来源于高德地图行政区数据
    
    BaseActivity
      // copy 高德地图行政区域数据到数据库
    //        initData();
    
三. UI展现

    private void showPopupWindow() {
            if (mPopupWindow != null) {
                mPopupWindow.setUp();
                mPopupWindow.showAtLocation(mRootLayout, Gravity.BOTTOM, 0, 0);
            }
        }