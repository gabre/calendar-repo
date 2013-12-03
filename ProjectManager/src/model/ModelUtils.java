package model;

public class ModelUtils {
	public static interface Getter {
		public int getInt(Object o);
	}
	
	public static class DurationGetter implements Getter {
		@Override
		public int getInt(Object o) {
			ProjectStep step = (ProjectStep)o;
			return step.getDuration();
		}
	}
	
	public static class CostGetter implements Getter {
		@Override
		public int getInt(Object o) {
			ProjectStep step = (ProjectStep)o;
			return step.getCost();
		}
	}
}
