package newlang8;

public interface Value {
// �������ׂ��R���X�g���N�^
//    public Value(String s);
//    public Value(int i);
//    public Value(double d);
//    public Value(boolean b);
//    public String get_sValue();
	public String getSValue();
	// �X�g�����O�^�Œl�����o���B�K�v������΁A�^�ϊ����s���B
    public int getIValue();
    	// �����^�Œl�����o���B�K�v������΁A�^�ϊ����s���B
    public double getDValue();
    	// �����_�^�Œl�����o���B�K�v������΁A�^�ϊ����s���B
    public boolean getBValue();
    	// �_���^�Œl�����o���B�K�v������΁A�^�ϊ����s���B
    public ValueType getType();
    public String toString();
    	// getSValue�i�j�Ɠ����B�֗��̂��߁B
}
