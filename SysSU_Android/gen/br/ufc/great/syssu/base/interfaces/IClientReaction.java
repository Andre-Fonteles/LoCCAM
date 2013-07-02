/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Andre\\workspace\\mestrado\\CAM\\LoCCAM\\SysSU_Android\\src\\br\\ufc\\great\\syssu\\base\\interfaces\\IClientReaction.aidl
 */
package br.ufc.great.syssu.base.interfaces;
// Declare the interface.

public interface IClientReaction extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements br.ufc.great.syssu.base.interfaces.IClientReaction
{
private static final java.lang.String DESCRIPTOR = "br.ufc.great.syssu.base.interfaces.IClientReaction";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an br.ufc.great.syssu.base.interfaces.IClientReaction interface,
 * generating a proxy if needed.
 */
public static br.ufc.great.syssu.base.interfaces.IClientReaction asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof br.ufc.great.syssu.base.interfaces.IClientReaction))) {
return ((br.ufc.great.syssu.base.interfaces.IClientReaction)iin);
}
return new br.ufc.great.syssu.base.interfaces.IClientReaction.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_react:
{
data.enforceInterface(DESCRIPTOR);
br.ufc.great.syssu.base.Tuple _arg0;
if ((0!=data.readInt())) {
_arg0 = br.ufc.great.syssu.base.Tuple.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.react(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements br.ufc.great.syssu.base.interfaces.IClientReaction
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void react(br.ufc.great.syssu.base.Tuple tuple) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((tuple!=null)) {
_data.writeInt(1);
tuple.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_react, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_react = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void react(br.ufc.great.syssu.base.Tuple tuple) throws android.os.RemoteException;
}
