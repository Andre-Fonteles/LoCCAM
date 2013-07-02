/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Andre\\workspace\\mestrado\\CAM\\LoCCAM\\SysSU_Android\\src\\br\\ufc\\great\\syssu\\base\\interfaces\\ISysSUService.aidl
 */
package br.ufc.great.syssu.base.interfaces;
// Declare the interface.

public interface ISysSUService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements br.ufc.great.syssu.base.interfaces.ISysSUService
{
private static final java.lang.String DESCRIPTOR = "br.ufc.great.syssu.base.interfaces.ISysSUService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an br.ufc.great.syssu.base.interfaces.ISysSUService interface,
 * generating a proxy if needed.
 */
public static br.ufc.great.syssu.base.interfaces.ISysSUService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof br.ufc.great.syssu.base.interfaces.ISysSUService))) {
return ((br.ufc.great.syssu.base.interfaces.ISysSUService)iin);
}
return new br.ufc.great.syssu.base.interfaces.ISysSUService.Stub.Proxy(obj);
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
case TRANSACTION_put:
{
data.enforceInterface(DESCRIPTOR);
br.ufc.great.syssu.base.Tuple _arg0;
if ((0!=data.readInt())) {
_arg0 = br.ufc.great.syssu.base.Tuple.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
this.put(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_read:
{
data.enforceInterface(DESCRIPTOR);
br.ufc.great.syssu.base.Pattern _arg0;
if ((0!=data.readInt())) {
_arg0 = br.ufc.great.syssu.base.Pattern.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
br.ufc.great.syssu.base.interfaces.IFilter _arg1;
_arg1 = br.ufc.great.syssu.base.interfaces.IFilter.Stub.asInterface(data.readStrongBinder());
java.util.List<br.ufc.great.syssu.base.Tuple> _result = this.read(_arg0, _arg1);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_take:
{
data.enforceInterface(DESCRIPTOR);
br.ufc.great.syssu.base.Pattern _arg0;
if ((0!=data.readInt())) {
_arg0 = br.ufc.great.syssu.base.Pattern.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
br.ufc.great.syssu.base.interfaces.IFilter _arg1;
_arg1 = br.ufc.great.syssu.base.interfaces.IFilter.Stub.asInterface(data.readStrongBinder());
java.util.List<br.ufc.great.syssu.base.Tuple> _result = this.take(_arg0, _arg1);
reply.writeNoException();
reply.writeTypedList(_result);
return true;
}
case TRANSACTION_subscribe:
{
data.enforceInterface(DESCRIPTOR);
br.ufc.great.syssu.base.interfaces.IClientReaction _arg0;
_arg0 = br.ufc.great.syssu.base.interfaces.IClientReaction.Stub.asInterface(data.readStrongBinder());
java.lang.String _arg1;
_arg1 = data.readString();
br.ufc.great.syssu.base.Pattern _arg2;
if ((0!=data.readInt())) {
_arg2 = br.ufc.great.syssu.base.Pattern.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
br.ufc.great.syssu.base.interfaces.IFilter _arg3;
_arg3 = br.ufc.great.syssu.base.interfaces.IFilter.Stub.asInterface(data.readStrongBinder());
this.subscribe(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_printLoCCAMState:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.printLoCCAMState();
reply.writeNoException();
reply.writeString(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements br.ufc.great.syssu.base.interfaces.ISysSUService
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
@Override public void put(br.ufc.great.syssu.base.Tuple tuple) throws android.os.RemoteException
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
mRemote.transact(Stub.TRANSACTION_put, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.util.List<br.ufc.great.syssu.base.Tuple> read(br.ufc.great.syssu.base.Pattern pattern, br.ufc.great.syssu.base.interfaces.IFilter filter) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<br.ufc.great.syssu.base.Tuple> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((pattern!=null)) {
_data.writeInt(1);
pattern.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder((((filter!=null))?(filter.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_read, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(br.ufc.great.syssu.base.Tuple.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public java.util.List<br.ufc.great.syssu.base.Tuple> take(br.ufc.great.syssu.base.Pattern pattern, br.ufc.great.syssu.base.interfaces.IFilter filter) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List<br.ufc.great.syssu.base.Tuple> _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((pattern!=null)) {
_data.writeInt(1);
pattern.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder((((filter!=null))?(filter.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_take, _data, _reply, 0);
_reply.readException();
_result = _reply.createTypedArrayList(br.ufc.great.syssu.base.Tuple.CREATOR);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void subscribe(br.ufc.great.syssu.base.interfaces.IClientReaction clientReaction, java.lang.String event, br.ufc.great.syssu.base.Pattern pattern, br.ufc.great.syssu.base.interfaces.IFilter filter) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((clientReaction!=null))?(clientReaction.asBinder()):(null)));
_data.writeString(event);
if ((pattern!=null)) {
_data.writeInt(1);
pattern.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder((((filter!=null))?(filter.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_subscribe, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public java.lang.String printLoCCAMState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_printLoCCAMState, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_put = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_read = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_take = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_subscribe = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_printLoCCAMState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public void put(br.ufc.great.syssu.base.Tuple tuple) throws android.os.RemoteException;
public java.util.List<br.ufc.great.syssu.base.Tuple> read(br.ufc.great.syssu.base.Pattern pattern, br.ufc.great.syssu.base.interfaces.IFilter filter) throws android.os.RemoteException;
public java.util.List<br.ufc.great.syssu.base.Tuple> take(br.ufc.great.syssu.base.Pattern pattern, br.ufc.great.syssu.base.interfaces.IFilter filter) throws android.os.RemoteException;
public void subscribe(br.ufc.great.syssu.base.interfaces.IClientReaction clientReaction, java.lang.String event, br.ufc.great.syssu.base.Pattern pattern, br.ufc.great.syssu.base.interfaces.IFilter filter) throws android.os.RemoteException;
public java.lang.String printLoCCAMState() throws android.os.RemoteException;
}
