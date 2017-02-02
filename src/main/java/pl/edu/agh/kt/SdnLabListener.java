package pl.edu.agh.kt;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.OFPort;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
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
		Hosts.put(IPv4Address.of("10.0.0.3"), 3);
		Hosts.put(IPv4Address.of("10.0.0.4"), 4);
		Hosts.put(IPv4Address.of("10.0.0.5"), 5);
		Hosts.put(IPv4Address.of("10.0.0.6"), 6);

		logger.info("************* NEW PACKET IN *************");
		// PacketExtractor extractor = new PacketExtractor();
		// extractor.packetExtract(cntx);

		// sending PacketOut
		OFPacketIn pin = (OFPacketIn) msg;
		OFPort outPort = OFPort.of(0);
		// rozdzielanie ruchu
		if (pin.getInPort() == OFPort.of(3) || pin.getInPort() == OFPort.of(4) || pin.getInPort() == OFPort.of(5)
				|| pin.getInPort() == OFPort.of(6)) {
			outPort = OFPort.of(this.dispatch());
		}
		else{
			Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
			if (eth.getEtherType() == EthType.IPv4){
				IPv4 ip = (IPv4) eth.getPayload();
				IPv4Address dstIp = ip.getDestinationAddress();
				outPort = OFPort.of(Hosts.get(dstIp));
			}
			return Command.CONTINUE;
		}

		// if (pin.getInPort() == OFPort.of(1)) {
		// outPort = OFPort.of(2);
		// } else
		// outPort = OFPort.of(1);
//
		Flows.simpleAdd(sw, pin, cntx, outPort);
		Flows.sendPacketOut(sw);

		return Command.STOP;
		// return Command.CONTINUE;
	}

	public int dispatch() {
		/*
		 * wybiera na jaki port przescac pakiet
		 */
		Random rn = new Random();
		// max to 100
		int max = 100;
		int load1 = rn.nextInt(100) % max;
		int load2 = rn.nextInt(100) % max;
		logger.info("Load1 is equal {}% load2 is equal {}%",load1, load2);
		int outPort = 1;
		if (load1 <= load2)
			outPort = 1;
		else
			outPort = 2;
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
