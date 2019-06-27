package android.support.design.widget;

import android.view.View;

import static android.support.v4.view.ViewCompat.TYPE_TOUCH;

/**
 * @author: xuzhiyong
 * @date: 2018/10/25 14:20
 * @Email: 18971269648@163.com
 * @description: 描述
 */
public class CoordinatorHelper {


    public static void setNestedScrollAccepted(View paramView)
    {
        if ((paramView.getLayoutParams() instanceof CoordinatorLayout.LayoutParams))
            ((CoordinatorLayout.LayoutParams)paramView.getLayoutParams()).setNestedScrollAccepted(TYPE_TOUCH, true);
    }
}
