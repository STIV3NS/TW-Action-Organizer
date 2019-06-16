package twao;

import pmsender.PMFormatter;
import twao.assigners.AssignerBuilder;
import twao.loaders.AllyLoader;
import twao.loaders.TargetsLoader;
import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class Demo {
    /**
     * Just a stupid simple hard-coded example
     * */
    public static void main(String[] args) throws Exception {
        AllyLoader al = new AllyLoader("allies.txt");
        al.setNicknameKey("nick");
        al.setNobleKey("nobles");
        al.setVillagesKey("villages");
        al.load();

        List<Player> players = al.getPlayers();
        List<AllyVillage> villages = al.getVillages();

        List<TargetVillage> targets = new LinkedList<>();
        List<TargetVillage> fake_noble_targets = new LinkedList<>();
        TargetsLoader.load("targets.txt", 10, targets);
        TargetsLoader.load("targets2.txt", 15, targets);
        TargetsLoader.load("noble_targets.txt", 6, fake_noble_targets);

        Thread t1 = new Thread(new AssignerBuilder()
                                    .setTargets(targets)
                                    .setAttackingVillages(villages)
                                    .setRelativityPoint(new Village(500, 500))
                                    .setType(AssignerBuilder.AssignerType.RAM)
                                    .build());

        Thread t2 = new Thread(new AssignerBuilder()
                                    .setTargets(fake_noble_targets)
                                    .setAttackingVillages(villages)
                                    .setRelativityPoint(new Village(500, 500))
                                    .setType(AssignerBuilder.AssignerType.FAKE_NOBLE)
                                    .setMaxNobleRange(500*500)
                                    .build());


        t1.start();
        t2.start();
        t1.join();
        t2.join();


        PMFormatter pf = new PMFormatter(
                new World("pl141.plemiona.pl"),
                new GregorianCalendar(1900, Calendar.JANUARY, 1)
        );
        for (Player p : players) {
            System.out.println( pf.get(p) );
        }
    }
}
