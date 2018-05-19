package engine.core;

/**
 * @program: regexengine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 12:15
 **/

/**
 * NFAState 和 DFAState 的基类
 */
public class State {
    private int stateID;
    private boolean acceptable;

    public State(int stateID){
        this.stateID = stateID;
        this.acceptable = false;
    }

    /**
     * @return: 当前状态是否是可接受状态
     */
    public boolean isAcceptable(){
        return this.acceptable;
    }

    /**
     * 设置状态为可接受状态
     */
    public void setAcceptable(){
        this.acceptable = true;
    }

    public void setStateID(int stateID){
        this.stateID = stateID;
    }

    public int getStateID(){
        return this.stateID;
    }
}
