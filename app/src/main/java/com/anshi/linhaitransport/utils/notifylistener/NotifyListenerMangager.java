package com.anshi.linhaitransport.utils.notifylistener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by yulu on 2018/3/13.
 */

public class NotifyListenerMangager {
private static NotifyListenerMangager manager;

private ArrayList<INotifyListener> listeners = new ArrayList();

private Map<String,INotifyListener> maps =new HashMap();



//单例模式​

public static NotifyListenerMangager getInstance() {

    if (manager == null) {

        manager = new NotifyListenerMangager();

    }

    return manager;
}


//注册监听

public void registerListener(INotifyListener lister,String tag){
    if(listeners.contains(lister))
        return;

           listeners.add(lister);

           maps.put(tag,lister);

}



//去除监听​

public void unRegisterListener(INotifyListener lister){
        if(listeners.contains(lister)){
            listeners.remove(lister);
        }

       if(maps.get(lister) != null){
           maps.remove(lister);

       }
}



//向所有注册页面发通知​

public void nofityAllContext(Object obj){

            for (INotifyListener lister : listeners) {

                lister.notifyContext(obj);

     }

}



//向某一页面发通知

public void nofityContext(Object obj, String tag){

            INotifyListener lister =  maps.get(tag);

       if(lister != null){

                lister.notifyContext(obj);

}

}



//去除所有监听,建议系统退出时

        public void removeAllListener(){

            listeners.clear();
            maps.clear();
        }
}