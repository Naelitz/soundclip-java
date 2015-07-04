package soundclip;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import soundclip.annotation.CueMeta;
import soundclip.api.Cue;
import soundclip.exception.CueTypeNotRegisteredException;

/**
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
 *
 * Created by nathan on 6/12/15
 */
public class CueFactory {

    private static final Logger log = LogManager.getLogger(CueFactory.class);

    private static HashMap<CueMeta, Class<? extends Cue>> cueStore = new HashMap<>();

    static{
        Reflections core = new Reflections("soundclip.core");
        discover(core.getSubTypesOf(Cue.class));
    }

    public static void discover(String path){
        log.info("Discovering plugins in " + path);
        //TODO: Discover path
    }

    private static void discover(Set<Class<? extends Cue>> classes){
        for(Class<? extends Cue> c : classes){
            if(c.isAnnotationPresent(CueMeta.class)){
                CueMeta meta = c.getAnnotation(CueMeta.class);

                log.info("Found " + c.getSimpleName() +
                        " [Author: " + meta.author() +
                        ", ID: " + meta.id() +
                        ", Category: " + meta.category().toString() +
                        ", Supported Versions: " + meta.supportedVersion() + "]");

                if(SoundClip.VERSION.satisfies(meta.supportedVersion())){
                    log.error(c.getSimpleName() + " is not supported in this version");
                }else{
                    cueStore.put(meta, c);
                }
            }else{
                log.error(c.getName() + " is missing the CueMeta annotation and cannot be registered");
            }
        }
    }

    public static CueMeta getMetadata(String id){
        return cueStore.keySet().stream().filter((meta) -> meta.id().equals(id)).findFirst().orElseGet(() -> null);
    }

    public static Cue build(String id) throws CueTypeNotRegisteredException, IllegalAccessException, InstantiationException {
        CueMeta meta = getMetadata(id);

        if(meta == null) throw new CueTypeNotRegisteredException(id);

        Class<? extends Cue> clazz = cueStore.get(meta);

        return clazz.newInstance();
    }

    public static Set<CueMeta> listAvailable(){
        return Collections.unmodifiableSet(cueStore.keySet());
    }
}
