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
  leftHost1 = self.addHost( 'Server1' )
  leftHost2 = self.addHost( 'Server2' )
  Switch = self.addSwitch( 's1' )
  rightHost1 = self.addHost( 'h1' )
  rightHost2 = self.addHost( 'h2' )
  rightHost3 = self.addHost( 'h3' )
  rightHost4 = self.addHost( 'h4' )
  # Add links
  self.addLink( leftHost1, Switch )
  self.addLink( leftHost2, Switch )
  self.addLink( rightHost1, Switch )
  self.addLink( rightHost2, Switch )
  self.addLink( rightHost3, Switch )
  self.addLink( rightHost4, Switch )
topos = { 'mytopo': ( lambda: MyTopo() ) } 
