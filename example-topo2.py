"""Custom topology example
Two directly connected switches plus a host for each switch:
 host --- switch --- switch --- host
Adding the 'topos' dict with a key/value pair to generate our newly defined
topology enables one to pass in '--topo=mytopo' from the command line.
"""
from mininet.topo import Topo
from mininet.node import Host
class MyTopo( Topo ):
 "Simple topology example."
 def __init__( self ):
  "Create custom topo."
  # Initialize topology
  Topo.__init__( self )
  # Add hosts and switches
  leftHost1 = self.addHost( 'Server1', mac='00:00:00:00:00:01' )
  leftHost2 = self.addHost( 'Server2', mac='00:00:00:00:00:02' )
  Switch = self.addSwitch( 's1', mac='00:00:00:00:00:10' )
  rightHost1 = self.addHost( 'h1', mac='00:00:00:00:00:03' )
  rightHost2 = self.addHost( 'h2', mac='00:00:00:00:00:04' )
  rightHost3 = self.addHost( 'h3', mac='00:00:00:00:00:05' )
  rightHost4 = self.addHost( 'h4', mac='00:00:00:00:00:06' )
  
 # leftHost1.setMAC( "00:00:00:00:00:01" )
  #leftHost2.setMAC( "00:00:00:00:00:02" )
  #rightHost1.setMAC( "00:00:00:00:00:03" )

  # Add links
  self.addLink( leftHost1, Switch )
  self.addLink( leftHost2, Switch )
  self.addLink( rightHost1, Switch )
  self.addLink( rightHost2, Switch )
  self.addLink( rightHost3, Switch )
  self.addLink( rightHost4, Switch )
topos = { 'mytopo': ( lambda: MyTopo() ) } 
