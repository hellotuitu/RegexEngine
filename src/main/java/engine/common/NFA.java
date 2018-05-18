package engine.common;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @program: regexengine
 * @description:
 * @author: TUITU
 * @create: 2018-05-17 12:12
 **/
public class NFA {
    private int stateCounter = 0;
    private ArrayList<NFAState> states;
    private NFAState startState;

    public NFA(){
        states = new ArrayList<>();
    }

    public static NFA constructNFAFromXML(String fileName) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(fileName));
        Element root = document.getRootElement();

        NFA nfa = new NFA();

        // 第一遍遍历 创建所有状态
        for (Iterator<Element> it = root.elementIterator("state"); it.hasNext();) {
            Element stateEle = it.next();
            int id = Integer.valueOf(stateEle.element("id").getTextTrim());
            NFAState state = new NFAState(id);
            if("true".equals(stateEle.element("accept").getTextTrim())){
                state.setAcceptable();
            }
            nfa.stateCounter++;
            nfa.states.add(state);
        }

        // 第二遍遍历 创建转移关系
        for (Iterator<Element> it = root.elementIterator("state"); it.hasNext();) {
            Element stateEle = it.next();
            int id = Integer.valueOf(stateEle.element("id").getTextTrim());

            for(Object obj : stateEle.elements("transition")){
                Element transition = (Element)obj;
                String keyDesc = transition.element("key").getTextTrim();

                char key = 0;
                if(!Const.EPSILON.equals(keyDesc)){
                    key = keyDesc.charAt(0);
                }

                int targetID = Integer.valueOf(transition.element("id").getTextTrim());
                nfa.states.get(id).addNextState(key, nfa.states.get(targetID));
            }
        }

        return nfa;
    }

    public int getStateCounterAndIncrement(){
        return stateCounter++;
    }

    public NFAState newState(){
        NFAState ns = new NFAState(getStateCounterAndIncrement());
        states.add(ns);

        return ns;
    }

    public ArrayList<NFAState> getStates(){
        return states;
    }

    public void setStartState(NFAState state){
        this.startState = state;
    }

    public NFAState getStartState(){
        return  this.startState;
    }

    public void detectAcceptableStates(){
        for(NFAState state : states){
            if(state.getNextStates().size() == 0){
                state.setAcceptable();
            }
        }
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("NFA with " + this.stateCounter + " states.\n\n");
        for(NFAState state : states){
            sb.append(state.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
