# Skylands Mobs Mod - Development Issues & Solutions Documentation

## Project Overview
- **Mod ID**: `skylandsmobs`
- **Minecraft Version**: 1.21.1
- **Mod Loader**: NeoForge 21.1.243
- **Template**: neoforge 1.21.1 mod template
- **Custom Entity**: Skyland Mob (flying, tamable, drops levitite_sharp)

---

## Issues Encountered & Solutions

### 1. Duplicate Class Definitions (Major Issue)
**Problem**: Multiple duplicate class definitions in the same file causing "already defined" errors.

**Error Messages**:
```
Variable DATA_FLAGS_ID is already defined in class SkylandMob
Variable FLAG_FLYING is already defined in class SkylandMob
Method registerGoals() is already defined in class SkylandMob
```

**Root Cause**: Multiple write operations to the same file without removing the old content first. The file contained duplicate class definitions.

**Solution**: 
- Delete the file completely before writing new content
- Use `del` command before `write` operation
- Or use `edit` with unique surrounding context

**Prevention**: 
- Always delete file before full rewrite: `del "path/to/file.java"` then `write`
- Use version control (git) to track changes
- Use IDE with proper file management

---

### 2. AirAndWaterRandomPos.getPos() API Signature Mismatch
**Problem**: Method signature changed in newer Minecraft versions.

**Error**:
```
Method getPos in class AirAndWaterRandomPos cannot be applied to given types
Required: PathfinderMob,int,int,int,double,double,double
Actual: SkylandMob,int,int
```

**Root Cause**: API signature changed in Minecraft 1.21.1. The method now requires more parameters.

**Solution**:
```java
// Old (broken):
return AirAndWaterRandomPos.getPos(mob, 16, 8);

// Fixed - use LandRandomPos for flying mobs:
return LandRandomPos.getPos(mob, 16, 8);

// Or use correct AirAndWaterRandomPos signature:
return AirAndWaterRandomPos.getPos(mob, 16, 8, mob.position(), (float)Math.PI / 2);
```

**Reference**: [Minecraft Wiki - RandomPos](https://minecraft.wiki/w/Random_position_generation)

---

### 3. TamableAnimal vs Monster Base Class
**Problem**: Tamable goals (`SitWhenOrderedToGoal`, `FollowOwnerGoal`, `OwnerHurtByTargetGoal`) require `TamableAnimal` base class, but entity extends `Monster`.

**Errors**:
```
SkylandMob cannot be converted to TamableAnimal
Constructor FollowOwnerGoal cannot be applied to given types
Symbol OwnerHurtByTargetGoal not found
```

**Solution**: 
- Don't use vanilla taming goals directly
- Implement custom goals that work with `Monster` base class
- Implement custom taming logic in `mobInteract()`

```java
// Custom follow goal for flying mobs
public static class FollowOwnerFlyingGoal extends Goal { ... }

// Custom sit goal
public static class SitWhenOrderedToFlyingGoal extends Goal { ... }
```

---

### 4. Missing Texture File
**Problem**: Entity texture not found.

**Error**:
```
Failed to load texture: skylandsmobs:textures/entity/skyland_mob.png
java.io.FileNotFoundException: skylandsmobs:textures/entity/skyland_mob.png
```

**Solution**:
1. Create texture file: `src/main/resources/assets/skylandsmobs/textures/entity/skyland_mob.png`
2. Model must reference correct texture in renderer
3. Texture must be 64x64 or 128x128 PNG

```java
// In renderer:
private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
    SkylandsMobs.MODID, "textures/entity/skyland_mob.png");
```

---

### 5. Model Issues - Head Rotation & Sitting

**Problem 1**: Head rotating independently when it's a child of body
**Problem 2**: Sitting not working properly
**Problem 3**: Head not staying attached to body

**Solution**:
```java
@Override
public void setupAnim(T entity, float limbSwing, float limbSwingAmount, 
                      float ageInTicks, float netHeadYaw, float headPitch) {
    // Head is child of wholeBody - NO independent rotation!
    // Head follows body automatically
    
    if (entity.isTame() && entity.isOrderedToSit()) {
        // Sitting pose
        this.wholeBody.xRot = (float)Math.PI / 2F;
        this.wholeBody.y = 20.0F;
        // Fold legs...
    } else {
        // Flying animation
        float flapAngle = Mth.sin(ageInTicks * 0.3F) * 0.3F;
        // leg animations...
        this.wholeBody.xRot = 0;
        this.wholeBody.y = 16.0F;
    }
}
```

**Reference**: [Minecraft Model Documentation](https://github.com/MinecraftForge/MinecraftForge/blob/1.21.x/src/main/java/net/minecraft/client/model/EntityModel.java)

---

### 6. Texture Not Loading (White/Purple Block)
**Problem**: Model loaded but texture shows as missing (purple/black checkerboard)

**Causes**:
1. Texture file missing or 0 bytes
2. Model JSON not referencing texture correctly
3. UV coordinates wrong in model

**Fix**:
1. Create proper 64x64 PNG texture
2. Ensure model UV coordinates match texture
3. Use `minecraft:item/generated` as parent for items, or proper entity texture for entities

```json
// For spawn egg item model:
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "minecraft:item/spawn_egg",
    "layer1": "minecraft:item/spawn_egg_overlay"
  }
}
```

---

### 7. Server Compatibility Issues
**Problem**: Mod compiled for NeoForge 21.1.244 but server runs 21.1.243

**Error**:
```
Mod skylandsmobs requires neoforge 21.1.244 or above
Currently, neoforge is 21.1.243
```

**Solution**:
```properties
# gradle.properties
neo_version=21.1.243
```

---

### 8. Duplicate Constant Definitions
**Problem**: Constants defined multiple times in same class.

**Error**:
```
Variable DATA_FLAGS_ID is already defined in class SkylandMob
Variable FLAG_FLYING is already defined in class SkylandMob
```

**Root Cause**: File written multiple times without deleting old content.

**Solution**: 
- Delete file before rewriting
- Use `del` then `write` pattern

---

### 9. Configuration Cache Issues
**Problem**: Gradle configuration cache causing stale builds.

**Solution**:
```bash
./gradlew build --no-configuration-cache
# Or clear cache:
rm -rf .gradle
./gradlew --stop
```

---

## Best Practices & Tips

### Development Workflow
1. **Always delete file before full rewrite**:
   ```bash
   del "src/main/java/.../SkylandMob.java"
   write "SkylandMob.java" with new content
   ```

2. **Use git for version control**:
   ```bash
   git init
   git add .
   git commit -m "Initial commit"
   ```

3. **Test on both client and server**:
   ```bash
   ./gradlew runClient    # Client test
   ./gradlew runServer    # Server test
   ```

### Model Development Tips
1. **Head is child of body**: Don't rotate head independently if it's a child of body
2. **Sitting animation**: Use parrot as reference (`ParrotModel`)
3. **Texture coordinates**: Match UV coordinates to actual texture
4. **Debug**: Use F3+B in-game to see hitboxes and model parts

### Tamable Entity Best Practices
1. **Don't extend TamableAnimal** if you need Monster features
2. **Implement custom taming logic** in `mobInteract()`
3. **Use flags** for tamed/sitting/flying states
4. **Sync data** with `SynchedEntityData`

### Server-Client Sync
1. **Data accessors**: Use `SynchedEntityData` for server-client sync
2. **Events**: Use `level().broadcastEntityEvent(this, (byte)7)` for taming particles
3. **Server-side only**: Check `!level().isClientSide` before modifying data

---

## Useful Resources

### Official Documentation
- [NeoForge Documentation](https://docs.neoforged.net/)
- [Minecraft Wiki - Entities](https://minecraft.wiki/w/Entity)
- [Minecraft Wiki - Models](https://minecraft.wiki/w/Model)

### GitHub References
- [NeoForge Example Mods](https://github.com/neoforged/ExampleMod)
- [Vanilla Entity Classes](https://github.com/minecraft/minecraft/blob/1.21.1/src/main/java/net/minecraft/world/entity/)

### Tools
- **Blockbench**: Model creation and animation
- **IntelliJ IDEA**: Best IDE for Minecraft modding
- **Gradle**: Build automation

---

## Quick Reference - Common Fixes

| Issue | Quick Fix |
|-------|-----------|
| Duplicate definitions | `del file.java` then `write` |
| API signature mismatch | Check Minecraft version docs |
| Missing texture | Create PNG at `assets/modid/textures/entity/` |
| Model not rendering | Check `renderToBuffer()` and texture path |
| Sitting not working | Check `isOrderedToSit()` logic |
| Head rotating | Don't rotate child parts independently |
| Server crash | Check mod version matches server |
| Duplicate definitions | Delete file before rewrite |

---

## Checklist for Future Development

- [ ] Delete file before full rewrite
- [ ] Test on both client and server
- [ ] Verify texture exists and loads
- [ ] Test taming/untaming flow
- [ ] Test sitting/standing
- [ ] Test death drops
- [ ] Test on dedicated server
- [ ] Verify no duplicate definitions
- [ ] Commit working version to git

---

## Next Steps for This Project

1. [ ] Fix texture file (create proper 64x64 PNG)
2. [ ] Test sitting animation in-game
3. [ ] Verify taming works with iron ingot
4. [ ] Test death drop (levitite_sharp)
5. [ ] Test on dedicated server
6. [ ] Add proper texture for skywhale model
7. [ ] Test heart particles on successful tame