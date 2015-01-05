package net.kados.gtp.app.libs.Interfaces;

public interface ParserMessages
{
    public abstract void sendMessage(String text);
    public abstract void sendProgress(double step, double max);
}