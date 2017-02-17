package pl.edu.agh.kt;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.types.ArpOpcode;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpDscp;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TransportPort;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.packet.ARP;
import net.floodlightcontroller.packet.Data;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPacket;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.packet.UDP;
import net.floodlightcontroller.core.IFloodlightProviderService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SdnLabListener implements IFloodlightModule, IOFMessageListener {

	protected IFloodlightProviderService floodlightProvider;
	protected static Logger logger;

	@Override
	public String getName() {
		return SdnLabListener.class.getSimpleName();
	}

	@Override
	public boolean isCallbackOrderingPrereq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCallbackOrderingPostreq(OFType type, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public net.floodlightcontroller.core.IListener.Command receive(IOFSwitch sw, OFMessage msg,
			FloodlightContext cntx) {
		Map<IPv4Address, Integer> Hosts = new HashMap();
		Hosts.put(IPv4Address.of("10.0.0.1"), 1);
		Hosts.put(IPv4Address.of("10.0.0.2"), 2);
		Hosts.put(IPv4Address.of("10.0.0.3"), 3);
		Hosts.put(IPv4Address.of("10.0.0.4"), 4);
		Hosts.put(IPv4Address.of("10.0.0.5"), 5);
		Hosts.put(IPv4Address.of("10.0.0.6"), 6);

		Map<IPv4Address, String> MACs = new HashMap();
		MACs.put(IPv4Address.of("10.0.0.1"), "00:00:00:00:00:01");
		MACs.put(IPv4Address.of("10.0.0.2"), "00:00:00:00:00:02");
		MACs.put(IPv4Address.of("10.0.0.3"), "00:00:00:00:00:03");
		MACs.put(IPv4Address.of("10.0.0.4"), "00:00:00:00:00:04");
		MACs.put(IPv4Address.of("10.0.0.5"), "00:00:00:00:00:05");
		MACs.put(IPv4Address.of("10.0.0.6"), "00:00:00:00:00:06");
		// TODO: dodac adresy MAC

		logger.info("************* NEW PACKET IN *************");
		// PacketExtractor extractor = new PacketExtractor();
		// extractor.packetExtract(cntx);

		// sending PacketOut
		OFPacketIn pin = (OFPacketIn) msg;
//		OFPort outPort = OFPort.of(0);
		// // rozdzielanie ruchu
		// TODO rozdzielis na ARP i IPv4
		Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
		// if (pin.getInPort() == OFPort.of(3) || pin.getInPort() ==
		// OFPort.of(4) || pin.getInPort() == OFPort.of(5)
		// || pin.getInPort() == OFPort.of(6)) {
		// outPort = OFPort.of(this.dispatch());
		// final IPv4 iPv4Packet = (IPv4) eth.getPayload();
		// final IPv4Address iPv4DestinationAddress =
		// iPv4Packet.getDestinationAddress();
		// Flows.simpleAdd(sw, pin, cntx, outPort);
		// Flows.sendPacketOut(sw);
		// return Command.STOP;
		// } else {
		// IPv4 ip = (IPv4) eth.getPayload();
		// IPv4Address dstIp = ip.getDestinationAddress();
		// outPort = OFPort.of(Hosts.get(dstIp));
		// Flows.simpleAdd(sw, pin, cntx, outPort);
		// Flows.sendPacketOut(sw);
		// return Command.STOP;
		// return Command.CONTINUE;
		// }
		if (eth.getEtherType() == EthType.IPv4) {
			logger.info("got IPv4");
			IPv4 ipv4 = (IPv4) eth.getPayload();

			/* More to come here */
		} else if (eth.getEtherType() == EthType.ARP) {
			logger.info("got ARP");
			ARP arp = (ARP) eth.getPayload();
			IPv4Address senderAddress = arp.getSenderProtocolAddress();
			MacAddress senderMAC = arp.getSenderHardwareAddress();
			IPv4Address targetAddress = arp.getTargetProtocolAddress();
			// MacAddress targetMAC = arp.getTargetHardwareAddress(); <- target
			// address to 00:00:00:00:00:00
//			 logger.info(senderMAC.toString());
			if (targetAddress.toString().equals("10.0.0.10")){
				logger.info("asdasdasd");
				logger.info(targetAddress.toString());
				IPv4Address serversAddress = dispatch();
				logger.info(MACs.get(serversAddress));
				OFPort outPort = OFPort.of(Hosts.get(serversAddress));
//				OFPort outPort = OFPort.of(Hosts.get(senderAddress));
				logger.info(outPort.toString());
				arp.setSenderHardwareAddress(MacAddress.of(MACs.get(serversAddress)));
				arp.setSenderProtocolAddress(serversAddress);
				arp.setOpCode(ArpOpcode.of(2));
//				arp.setSenderHardwareAddress(MacAddress.of("00:00:00:00:00:15"));
//				arp.setSenderProtocolAddress(targetAddress);
				arp.setTargetHardwareAddress(senderMAC);
				arp.setTargetProtocolAddress(senderAddress);
				
				OFPacketOut po = sw.getOFFactory().buildPacketOut().setData(arp.serialize())
						.setActions(Collections
								.singletonList((OFAction) sw.getOFFactory().actions().output(outPort, 0xffFFffFF)))
						.setInPort(OFPort.CONTROLLER).build();
				Flows.simpleAdd(sw, pin, cntx, outPort);
				sw.write(po);
			}else{
//				arp.setSenderHardwareAddress(MacAddress.of(MACs.get(serversAddress)));
				arp.setSenderProtocolAddress(IPv4Address.of("10.0.0.10"));
				logger.info("reszta");
				logger.info(targetAddress.toString());
				
				OFPacketOut po = sw.getOFFactory().buildPacketOut().setData(arp.serialize())
						.setActions(Collections
								.singletonList((OFAction) sw.getOFFactory().actions().output(OFPort.of(Hosts.get(arp.getTargetProtocolAddress())), 0xffFFffFF)))
						.setInPort(OFPort.CONTROLLER).build();
				sw.write(po);
			}
//			Flows.sendPacketOut(sw);

			/* More to come here */

		}
		return Command.CONTINUE;

	}

	public IPv4Address dispatch() {
		/*
		 * wybiera na jaki port przescac pakiet
		 */
		Random rn = new Random();
		// max to 100
		int max = 100;
		int load1 = rn.nextInt(100) % max;
		int load2 = rn.nextInt(100) % max + 100;
		logger.info("Load1 is equal {}% load2 is equal {}%", load1, load2);
		IPv4Address outPort;
		if (load1 <= load2)
			outPort = IPv4Address.of("10.0.0.1");
		else
			outPort = IPv4Address.of("10.0.0.2");
		return outPort;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleServices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
		Collection<Class<? extends IFloodlightService>> l = new ArrayList<Class<? extends IFloodlightService>>();
		l.add(IFloodlightProviderService.class);
		return l;
	}

	@Override
	public void init(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
		logger = LoggerFactory.getLogger(SdnLabListener.class);
	}

	@Override
	public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
		floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
		logger.info("******************* START **************************");

	}

}
