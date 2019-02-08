package gui.util;

import java.time.LocalDateTime;
import java.util.List;
import javafx.scene.chart.Axis;


public class LocalDateTimeAxis extends Axis<LocalDateTime> {
	
	
	
	@Override
	protected Object autoRange(double length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setRange(Object range, boolean animate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Object getRange() {
		// TODO Auto-generated method stub
		//Period start - ende
		return null;
	}

	@Override
	public double getZeroPosition() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDisplayPosition(LocalDateTime value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LocalDateTime getValueForDisplay(double displayPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValueOnAxis(LocalDateTime value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double toNumericValue(LocalDateTime value) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public LocalDateTime toRealValue(double value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<LocalDateTime> calculateTickValues(double length, Object range) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getTickMarkLabel(LocalDateTime value) {
		return null;
	}
	
	@Override
	public void invalidateRange(List<LocalDateTime> data) {
		// TODO Auto-generated method stub
		super.invalidateRange(data);
	}

}
