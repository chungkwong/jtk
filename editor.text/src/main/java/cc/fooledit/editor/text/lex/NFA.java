/*
 * Copyright (C) 2016,2017 Chan Chung Kwong <1m02math@126.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cc.fooledit.editor.text.lex;
import cc.fooledit.util.IntCheckPointIterator;
import cc.fooledit.util.Pair;
import java.util.*;
import java.util.stream.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public final class NFA{
	private final State init, accept;
	private static final boolean DEBUG=false;
	private static final HashMap<State,String> SYMBOLS=DEBUG?new HashMap<>():null;
	public NFA(){
		this.init=createState();
		this.accept=createState();
	}
	public Pair<StateSet,String> run(IntCheckPointIterator input){
		return run(input,init);
	}
	public Pair<StateSet,String> run(IntCheckPointIterator input,State start){
		StateSet set=new StateSet(start);
		StateSet lastAccept=set.contains(accept)?set:null;
		if(DEBUG)
			printTrace(set);
		StringBuilder buf=new StringBuilder();
		input.startPreread();
		while(!set.isEmpty()&&input.hasNext()){
			set.next(input.nextInt());
			if(DEBUG)
				printTrace(set);
			if(set.contains(accept)){
				for(int c:input.endPrereadForward())
					buf.appendCodePoint(c);
				lastAccept=set.clone();
				input.startPreread();
			}
		}
		input.endPrereadBackward();
		return new Pair<>(lastAccept,buf.toString());
	}
	private void printTrace(StateSet set){
		System.out.println(set.set.stream().map(SYMBOLS::get).collect(Collectors.joining(",")));
	}
	public boolean isAccepted(IntCheckPointIterator input){
		return isAccepted(input,init);
	}
	public boolean isAccepted(IntCheckPointIterator input,State start){
		StateSet result=run(input,start).getKey();
		return !input.hasNext()&&result!=null&&result.contains(accept);
	}
	public State getInitState(){
		return init;
	}
	public State getAcceptState(){
		return accept;
	}
	public State createState(){
		State state=new State();
		return state;
	}
	public void prepareForRun(){
		findLambdaClosure();
		if(DEBUG){
			buildDebugSymbols();
			System.out.println(this);
		}
	}
	private void findLambdaClosure(){
		boolean changed=true;
		outer:while(changed){
			changed=false;
			for(State state:getStates()){
				for(State next:state.lambdaTransitionTable){
					for(State nextnext:next.lambdaTransitionTable){
						if(!state.lambdaTransitionTable.contains(nextnext)){
							changed=true;
							state.lambdaTransitionTable.add(nextnext);
							continue outer;
						}
					}
				}
			}
		}
	}
	private void buildDebugSymbols(){
		int index=0;
		for(State s:getStates())
			SYMBOLS.put(s,Integer.toString(index++));
	}
	private Collection<State> getStates(){
		HashSet<State> states=new HashSet<>();
		Stack<State> tosearch=new Stack<>();
		states.add(init);
		tosearch.push(init);
		while(!tosearch.isEmpty()){
			State state=tosearch.pop();
			Set<State> found=state.lambdaTransitionTable.stream().filter((s)->!states.contains(s)).collect(Collectors.toSet());
			states.addAll(found);
			tosearch.addAll(found);
			found=state.transitionTable.stream().map((p)->p.getValue()).filter((s)->!states.contains(s)).collect(Collectors.toSet());
			states.addAll(found);
			tosearch.addAll(found);
		}
		return states;
	}
	public DFA toDFA(){//TODO
		Collection<State> states=getStates();
		DFA dfa=new DFA(null);
		DFA.State state=new DFA.State();


		return dfa;
	}
	public static class State{
		private final List<Pair<CharacterSet,State>> transitionTable=new LinkedList<>();
		private final HashSet<State> lambdaTransitionTable=new HashSet<>();
		State(){
		}
		public void addTransition(CharacterSet set,State next,boolean checkOverlap){
			if(checkOverlap&&transitionTable.stream().anyMatch((pair)->
					CharacterSetFactory.createIntersectionCharacterSet(pair.getKey(),set).stream().findAny().isPresent())){
				throw new AmbiguousException();
			}
			transitionTable.add(new Pair<>(set,next));
		}
		public void addLambdaTransition(State next){
			lambdaTransitionTable.add(next);
		}
		public void nextState(int codePoint,HashSet<State> result){
			Optional<Pair<CharacterSet,State>> found=transitionTable.stream().filter((pair)->pair.getKey().contains(codePoint)).findFirst();
			if(found.isPresent()){
				result.add(found.get().getValue());
				result.addAll(found.get().getValue().lambdaTransitionTable);
			}
		}
	}
	public static class TaggedState extends State{
		private final String tag;
		private final int id;
		public TaggedState(String tag,int id){
			this.tag=tag;
			this.id=id;
		}
		public String getTag(){
			return tag;
		}
		public int getId(){
			return id;
		}
	}
	public static class StateSet{
		private HashSet<State> set=new HashSet<>(),spare=new HashSet<>();
		public StateSet(){
		}
		public StateSet(State start){
			set.add(start);
			set.addAll(start.lambdaTransitionTable);
		}
		public boolean contains(State state){
			return set.contains(state);
		}
		public boolean isEmpty(){
			return set.isEmpty();
		}
		public void next(int codePoint){
			if(DEBUG)
				System.out.println((char)codePoint);
			set.stream().forEach((state)->state.nextState(codePoint,spare));
			HashSet<State> tmp=set;
			set=spare;
			spare=tmp;
			spare.clear();
		}
		public TaggedState getTaggedState(){
			return (TaggedState)set.stream().filter((s)->s instanceof TaggedState).findAny().orElse(null);
		}
		@Override
		protected StateSet clone(){
			StateSet copy=new StateSet();
			copy.set.addAll(set);
			return copy;
		}
		@Override
		public boolean equals(Object obj){
			return obj!=null&&obj instanceof StateSet&&((StateSet)obj).set.equals(set);
		}
		@Override
		public int hashCode(){
			int hash=7;
			hash=89*hash+Objects.hashCode(this.set);
			return hash;
		}
	}
	@Override
	public String toString(){
		if(!DEBUG)
			return super.toString();
		StringBuilder buf=new StringBuilder();
		SYMBOLS.forEach((s,n)->{
			buf.append(n).append("\n\t");
			s.lambdaTransitionTable.forEach((t)->buf.append(SYMBOLS.get(t)).append(','));
			s.transitionTable.forEach((pair)->buf.append("\n\t").append(pair.getKey()).append("->").append(SYMBOLS.get(pair.getValue())));
			buf.append("\n");
		});
		buf.append("init:").append(SYMBOLS.get(init)).append('\n');
		buf.append("accept:").append(SYMBOLS.get(accept)).append('\n');
		return buf.toString();
	}
	public static void main(String[] args){
		NFA matcher=RegularExpression.parseRegularExpression("\\040").toNFA();
		matcher.prepareForRun();
		System.out.println(matcher);
		System.out.println(matcher.run(new IntCheckPointIterator(" ".codePoints().iterator())));
	}
}