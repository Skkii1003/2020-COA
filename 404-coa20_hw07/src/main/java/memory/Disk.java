package memory;


import transformer.Transformer;
import util.CRC;

/**
 * 磁盘抽象类，磁盘大小为64M
 */
public class Disk {

    public static int DISK_SIZE_B = 64 * 1024 * 1024;      // 磁盘大小 64 MB

    private static Disk diskInstance = new Disk();

    /**
     * 请勿修改下列属性，至少不要修改一个扇区的大小，如果要修改请保证磁盘的大小为64MB
     */
    public static final int CYLINDER_NUM = 8;
    public static final int TRACK_PRE_PLATTER = 16;
    public static final int SECTOR_PRE_TRACK = 128;
    public static final int BYTE_PRE_SECTOR = 512;
    public static final int PLATTER_PRE_CYLINDER = 8;

    public static final String POLYNOMIAL = "11000000000100001";
    public disk_head DISK_HEAD = new disk_head();

    RealDisk disk = new RealDisk();

    /**
     * 初始化
     */
    private Disk() { }

    public static Disk getDisk() {
        return diskInstance;
    }

    /**
     * 读磁盘
     * @param eip
     * @param len
     * @return
     */
    public char[] read(String eip, int len) {
        //TODO
        Transformer t = new Transformer();
        char[] data = new char[len];
        int i = 0;
        DISK_HEAD.Seek(Integer.parseInt(t.binaryToInt(eip)));
        Sector s = disk.cylinders[DISK_HEAD.cylinder].platters[DISK_HEAD.platter].tracks[DISK_HEAD.track].sectors[DISK_HEAD.sector];
        while(len-->0){
            DISK_HEAD.adjust();
            s = disk.cylinders[DISK_HEAD.cylinder].platters[DISK_HEAD.platter].tracks[DISK_HEAD.track].sectors[DISK_HEAD.sector];
            data[i++] = s.dataField.Data[DISK_HEAD.point++];
            if (DISK_HEAD.point==512){
                CRC.Check(ToBitStream(s.dataField.Data),POLYNOMIAL,ToBitStream(s.dataField.CRC));
            }
        }
        return data;
    }

    /**
     * 写磁盘
     * @param eip
     * @param len
     * @param data
     */
    public void write(String eip, int len, char[] data) {
        //TODO
        Transformer t = new Transformer();
        int i = 0;
        DISK_HEAD.Seek(Integer.parseInt(t.binaryToInt(eip)));
        Sector s = disk.cylinders[DISK_HEAD.cylinder].platters[DISK_HEAD.platter].tracks[DISK_HEAD.track].sectors[DISK_HEAD.sector];
        while(len>0){
            DISK_HEAD.adjust();
            s = disk.cylinders[DISK_HEAD.cylinder].platters[DISK_HEAD.platter].tracks[DISK_HEAD.track].sectors[DISK_HEAD.sector];
            s.dataField.Data[DISK_HEAD.point] = data[i];
            DISK_HEAD.point++;
            i++;
            if (DISK_HEAD.point==512){
                s.dataField.CRC = ToByteStream(CRC.Calculate(ToBitStream(s.dataField.Data),POLYNOMIAL));
            }
            len--;
        }
        s.dataField.CRC = ToByteStream(CRC.Calculate(ToBitStream(s.dataField.Data),POLYNOMIAL));
    }

    /**
     * 写磁盘（地址为Integer型）
     * 测试会调用该方法
     * @param eip
     * @param len
     * @param data
     */
    public void write(int eip, int len, char[] data) {
        //TODO
        int i = 0;
        DISK_HEAD.Seek(eip);
        Sector s = disk.cylinders[DISK_HEAD.cylinder].platters[DISK_HEAD.platter].tracks[DISK_HEAD.track].sectors[DISK_HEAD.sector];
        while(len-->0){
            DISK_HEAD.adjust();
            s = disk.cylinders[DISK_HEAD.cylinder].platters[DISK_HEAD.platter].tracks[DISK_HEAD.track].sectors[DISK_HEAD.sector];
            s.dataField.Data[DISK_HEAD.point++] = data[i++];
            if (DISK_HEAD.point==512){
                s.dataField.CRC = ToByteStream(CRC.Calculate(ToBitStream(s.dataField.Data),POLYNOMIAL));
            }
        }
        s.dataField.CRC = ToByteStream(CRC.Calculate(ToBitStream(s.dataField.Data),POLYNOMIAL));
    }

    /**
     * 该方法仅用于测试
     */
    public char[] getCRC() {
        return disk.getCRC(DISK_HEAD);
    }

    /**
     * 磁头
     */
    private class disk_head {
        int cylinder;
        int platter;
        int track;
        int sector;
        int point;
        /**
         * 调整磁头的位置
         */
        public void adjust() {
            if (point == BYTE_PRE_SECTOR) {
                point = 0;
                sector++;
            }
            if (sector == SECTOR_PRE_TRACK) {
                sector = 0;
                track++;
            }
            if (track == TRACK_PRE_PLATTER) {
                track = 0;
                platter++;
            }
            if (platter == PLATTER_PRE_CYLINDER) {
                platter = 0;
                cylinder++;
            }
            if (cylinder == CYLINDER_NUM) {
                cylinder = 0;
            }
        }

        /**
         * 磁头回到起点
         */
        public void Init() {
//            try {
//                Thread.sleep(1000);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            cylinder = 0;
            track = 0;
            sector = 0;
            point = 0;
            platter = 0;
        }

        /**
         * 将磁头移动到目标位置
         * @param start
         */
        public void Seek(int start) {
//            try {
//                Thread.sleep(0);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            for (int i = cylinder; i < CYLINDER_NUM; i++) {
                for (int t = platter; t < PLATTER_PRE_CYLINDER; t++) {
                    for (int j = track; j < TRACK_PRE_PLATTER; j++) {
                        for (int z = sector; z < SECTOR_PRE_TRACK; z++) {
                            for (int k = point; k < BYTE_PRE_SECTOR; k++) {
                                if ((i * PLATTER_PRE_CYLINDER * TRACK_PRE_PLATTER * SECTOR_PRE_TRACK * BYTE_PRE_SECTOR + t * TRACK_PRE_PLATTER * SECTOR_PRE_TRACK * BYTE_PRE_SECTOR + j * SECTOR_PRE_TRACK * BYTE_PRE_SECTOR + z * BYTE_PRE_SECTOR + k) == start) {
                                    cylinder = i;
                                    track = j;
                                    sector = z;
                                    point = k;
                                    platter = t;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
            Init();
            Seek(start);
        }

        @Override
        public String toString() {
            return "The Head Of Disk Is In\n" +
                    "platter:\t" + cylinder + "\n" +
                    "track:\t\t" + track + "\n" +
                    "sector:\t\t" + sector + "\n" +
                    "point:\t\t" + point;
        }
    }

    /**
     * 600 Bytes/Sector
     */
    private class Sector {
        char[] gap1 = new char[17];
        IDField idField = new IDField();
        char[] gap2 = new char[41];
        DataField dataField = new DataField();
        char[] gap3 = new char[20];
    }

    /**
     * 7 Bytes/IDField
     */
    private class IDField {
        char SynchByte;
        char[] Track = new char[2];
        char Head;
        char sector;
        char[] CRC = new char[2];
    }

    /**
     * 515 Bytes/DataField
     */
    private class DataField {
        char SynchByte;
        char[] Data = new char[512];
        char[] CRC = new char[2];
    }

    /**
     * 128 sectors pre track
     */
    private class Track {
        Sector[] sectors = new Sector[SECTOR_PRE_TRACK];

        Track() {
            for (int i = 0; i < SECTOR_PRE_TRACK; i++) sectors[i] = new Sector();
        }
    }


    /**
     * 32 tracks pre platter
     */
    private class Platter {
        Track[] tracks = new Track[TRACK_PRE_PLATTER];

        Platter() {
            for (int i = 0; i < TRACK_PRE_PLATTER; i++) tracks[i] = new Track();
        }
    }

    /**
     * 8 platter pre Cylinder
     */
    private class Cylinder {
        Platter[] platters = new Platter[PLATTER_PRE_CYLINDER];

        Cylinder() {
            for (int i = 0; i < PLATTER_PRE_CYLINDER; i++) platters[i] = new Platter();
        }
    }


    private class RealDisk {
        Cylinder[] cylinders = new Cylinder[CYLINDER_NUM];

        public RealDisk() {
            for (int i = 0; i < CYLINDER_NUM; i++) cylinders[i] = new Cylinder();
        }

        public char[] getCRC(disk_head d) {
            return cylinders[d.cylinder].platters[d.platter].tracks[d.track].sectors[d.sector].dataField.CRC;
        }
    }

    /**
     * 将Byte流转换成Bit流
     * @param data
     * @return
     */
    public static char[] ToBitStream(char[] data) {
        char[] t = new char[data.length * 8];
        Transformer transformer = new Transformer();
        String result = "";
        //TODO
        for (char c:data){
            int size = (int) c;
            result = result + transformer.intToBinary(size+"").substring(24);
        }
        for (int i =0;i<result.length();i++){
            t[i] = result.charAt(i);
        }
        return t;
    }

    /**
     * 将Bit流转换为Byte流
     * @param data
     * @return
     */
    public static char[] ToByteStream(char[] data) {
        char[] t = new char[data.length / 8];
        Transformer transformer = new Transformer();
        //TODO
        for (int i = 0;i<t.length;i++){
            String s = "";
            for (int j=0;j<8;j++){
                s = s + data[8*i+j];
            }
            int size = Integer.parseInt(transformer.binaryToInt("000000000000000000000000"+s));
            t[i] = (char) size;
        }
        return t;
    }
    public static void main(String[] args){
        char[] c = {'0','1'};
        String s = "0011000000110001";
        char[] crc="k".toCharArray();
        System.out.println(ToByteStream(ToBitStream(crc)));
        //System.out.println(ToByteStream(s.toCharArray()));
    }

    /**
     * 这个方法仅供测试，请勿修改
     * @param eip
     * @param len
     * @return
     */
    public char[] readTest(String eip, int len){
        char[] data = read(eip, len);
        System.out.print(data);
        return data;
    }
}
