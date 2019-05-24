package com.factorys.error;

/*
=枚举类，友好的显示东西，也被称为数据字典
当service层需要某些数据加上描述，可以使用这个
 */
public enum SeckillStatEnum {
    SUCCESS(1,"秒杀成功"),
END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常")



    ;

    private int state;
    private String stateInfo;

    public int getState() {
        return state;
    }
    public String getStateInfo() {
        return stateInfo;
    }

    SeckillStatEnum(int state, String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }
    /**
     * 根据int类型的index,找到对应StateEnum
     * @param index
     * @return
     */
    public static SeckillStatEnum stateOf(int index){
        for (SeckillStatEnum state:values()){
            if (state.getState()==index){
                return state;
            }
        }
        return null;
    }

}
