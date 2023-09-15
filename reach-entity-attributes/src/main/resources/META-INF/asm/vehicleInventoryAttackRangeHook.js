var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var LdcInsnNode = Java.type('org.objectweb.asm.tree.LdcInsnNode');

function initializeCoreMod() {
    return {
        'vehicleInventoryAttackRangeHook': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.vehicle.VehicleInventory',
                'methodName': 'm_219954_',
                'methodDesc': '(Lnet/minecraft/world/entity/player/Player;)Z'
            },
            'transformer': function (node) {
                for (var i = 0; i < node.instructions.size(); i++) {
                    var insn = node.instructions.get(i);
                    if (insn instanceof LdcInsnNode && insn.cst === 8.0) {
                        var list = new InsnList();
                        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/jamieswhiteshirt/reachentityattributes/ReachEntityAttributes", "getAttackRangeDistance", "(ZLnet/minecraft/entity/player/PlayerEntity;)Z"));
                        node.instructions.insert(insn, list);
                        ASMAPI.log('DEBUG', 'Injected attach range check into VehicleInventory');
                        break;
                    }
                }
                return node;
            }
        }
    }
}