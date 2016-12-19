package cn.edu.fetchDocFromCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;





public class HtmlToText {

	protected static Map<String,String> _tags;
	protected static Set<String> _ignoreTags;
private static final String LINE_SEPARATOR=System.getProperty("line.separator");

	protected TextBuilder _text;
	protected String _html;
	protected int _pos;
	protected boolean selfClosing;

	static{
		_tags = new HashMap<String, String>();
		_tags.put("address", "\n");
		_tags.put("blockquote", "\n");
		_tags.put("div", "\n");
		_tags.put("dl", "\n");
		_tags.put("fieldset", "\n");
		_tags.put("form", "\n");
		_tags.put("h1", "\n");
		_tags.put("/h1", "\n");
		_tags.put("h2", "\n");
		_tags.put("/h2", "\n");
		_tags.put("h3", "\n");
		_tags.put("/h3", "\n");
		_tags.put("h4", "\n");
		_tags.put("/h4", "\n");
		_tags.put("h5", "\n");
		_tags.put("/h5", "\n");
		_tags.put("h6", "\n");
		_tags.put("/h6", "\n");
		_tags.put("p", "\n");
		_tags.put("/p", "\n");
		_tags.put("table", "\n");
		_tags.put("/table", "\n");
		_tags.put("ul", "\n");
		_tags.put("/ul", "\n");
		_tags.put("ol", "\n");
		_tags.put("/ol", "\n");
		_tags.put("/li", "\n");
		_tags.put("br", "\n");
		_tags.put("/td", "\t");
		_tags.put("/tr", "\n");
		_tags.put("/pre", "\n");

		_ignoreTags = new HashSet<String>();
		_ignoreTags.add("script");
		_ignoreTags.add("noscript");
		_ignoreTags.add("style");
		_ignoreTags.add("object");
		_ignoreTags.add("!--");
	}


	
	public String Convert(String html)
	{
		// Initialize state variables
		_text = new TextBuilder();
		_html = html;
		_pos = 0;

		//to avoid two words linked together
		int tempInt = 0;
		// Process input
		int CountBody=0;
		int _bodyF=0;
		while (!EndOfText())
		{

			if(_bodyF!=0){
				if (Peek() == '<'){
					String tag = ParseTag();
					if(!tag.equals("body")){
						_bodyF=0;
						CountBody++;
						continue;
					}

				}
			}
			if (Peek() == '<')
			{   tempInt = 1;
				// HTML tag
				String tag = ParseTag();
		//		System.out.print("("+tag+"@ "+_pos+" )");

				if (tag.equals("body"))
				{
					if(CountBody<1)
						//_text.Clear();
						;
					CountBody++;
					_bodyF=0;;
				}
				else	if (tag.equals("/body"))
				{
					_bodyF=1;
				}
				else if (tag.equals("pre"))
				{
					_text.setPreformatted(true);
					EatWhitespaceToNextLine();
				}
				else if (tag.equals("/pre"))
				{
					_text.setPreformatted(false);
				}

				String value="";
				Set<String> keyset=_tags.keySet();
				if (keyset.contains(tag))
				{
					value=_tags.get(tag);
					_text.Write(value);
				}

				if (_ignoreTags.contains(tag)){
					EatInnerContent(tag);
				}
			}
			else if (Character.isWhitespace(Peek()))
			{
				tempInt = 2;
				_text.Write(_text.getPreformatted() ? Peek() : ' ');
				MoveAhead();
			}
			else
			{ 
				if(tempInt!= 3)_text.Write(' ');
				tempInt = 3;
				_text.Write(Peek());
				MoveAhead();
			}
		}
/*		System.out.println();
		System.out.print("==>"+_text.toString());
		System.out.println("++>");*/
		return _text.toString();
	}

	// Eats all characters that are part of the current tag
	// and returns information about that tagselfClosing
	protected String ParseTag( )
	{//System.out.print("**");
		String tag ="";
		selfClosing = false;

		if (Peek() == '<')
		{
			MoveAhead();

	//	System.out.print("("+_pos+"~");
			// Parse tag name
			EatWhitespace();
			int start = _pos;
			if (Peek() == '/')
				MoveAhead();
			while (!EndOfText() && !Character.isWhitespace(Peek()) &&
				Peek() != '/' && Peek() != '>')
				MoveAhead();
			tag = _html.substring(start, _pos).toLowerCase();
//	System.out.println(tag);
			if((_pos<_html.length())&&(_pos>0)){
//	System.out.print(" "+_pos+_html.charAt(_pos)+")");
			}
			// Parse rest of tag
			while (!EndOfText() && Peek() != '>')
			{//System.out.print(_html.charAt(_pos));
				if (Peek() == '"' || Peek() == '\'')
				//	EatQuotedValue();
					MoveAhead();
				else
				{
					if (Peek() == '/')
						selfClosing = true;
					MoveAhead();
				}
			}

	//		System.out.println();
			MoveAhead();
		}

		return tag;
	}

	// Consumes inner content from the current tag ȥ�����Ա�ǩ�е��ı�
	protected void EatInnerContent(String tag)
	{
		String endTag = "/" + tag;

		while (!EndOfText())
		{
			if (Peek() == '<')
			{
				// Consume a tag
				tag=ParseTag();
				if (endTag.equals(tag))
					return;
				// Use recursion to consume nested tags
				if (!selfClosing && !tag.startsWith("/")){
					_pos-=(tag.length()+2);
					return;
				}
			}
			else MoveAhead();
		}
	}

	// Returns true if the current position is at the end of
	// the string �ж��Ƿ����
	protected boolean EndOfText()
	{
		return _pos >= _html.length(); 
	}

	// Safely returns the character at the current position ȡ��ǰ�ַ�
	protected char Peek()
	{
		//System.out.print((_pos < _html.length()) ? _html.charAt(_pos) : (char)0);
		return (_pos < _html.length()) ? _html.charAt(_pos) : (char)0;
	}

	// Safely advances to current position to the next character  ��ǰ��һ��
	protected void MoveAhead()
	{
		_pos = Math.min(_pos + 1, _html.length());
	}

	// Moves the current position to the next non-whitespace  ȥ���ո�
	// character.
	protected void EatWhitespace()
	{
		while (Character.isWhitespace(Peek()))
			MoveAhead();
	}

	// Moves the current position to the next non-whitespace
	// character or the start of the next line, whichever
	// comes first
	protected void EatWhitespaceToNextLine()
	{
		while (Character.isWhitespace(Peek()))
		{
			char c = Peek();
			MoveAhead();
			if (c == '\n')
				break;
		}
	}

	// Moves the current position past a quoted value
	protected void EatQuotedValue()
	{
		char c = Peek();
		if (c == '"' || c == '\'')
		{
		//	System.out.print(c);
			// Opening quote
			MoveAhead();
			// Find end of value
		//	int start = _pos;
			_pos = indexOfAny(_html,new char[] { c, '\r', '\n' }, _pos);
		//	System.out.print(_pos+"/");
			if (_pos < 0)
				_pos = _html.length();
			else
				MoveAhead();	// Closing quote
		}
	}


	// A StringBuilder class that helps eliminate excess whitespace.
	protected class TextBuilder
	{
		private StringBuilder _text;
		private StringBuilder _currLine;
		private int _emptyLines;
		private boolean _preformatted;

		// Construction
		public TextBuilder()
		{
			_text = new StringBuilder();
			_currLine = new StringBuilder();
			_emptyLines = 0;
			_preformatted = false;
		}

		/// <summary>
		/// Normally, extra whitespace characters are discarded.
		/// If this property is set to true, they are passed
		/// through unchanged.
		/// </summary>
		
		
		
		public boolean getPreformatted(){
			return _preformatted;
		}
		
public void setPreformatted(boolean value) {
	if (value) {
		if (_currLine.length() > 0)
			FlushCurrLine();
		_emptyLines = 0;
	}
	_preformatted = value;
}
		
		
		
	

		/// <summary>
		/// Clears all current text.
		/// </summary>
		public void Clear()
		{
			_text.setLength(0);
			_currLine.setLength(0);
			_emptyLines = 0;
		}

		/// <summary>
		/// Writes the given string to the output buffer.
		/// </summary>
		/// <param name="s"></param>
		public void Write(String s)
		{
			for(char c:s.toCharArray()){
				Write(c);
			}
		}

		/// <summary>
		/// Writes the given character to the output buffer.
		/// </summary>
		/// <param name="c">Character to write</param>
		public void Write(char c)
		{
			if (_preformatted)
			{
				// Write preformatted character
				_text.append(c);
			}
			else
			{
				if (c == '\r')
				{
					// Ignore carriage returns. We'll process
					// '\n' if it comes next
				}
				else if (c == '\n')
				{
					
					// Flush current line
					FlushCurrLine();
				}
				else if (Character.isWhitespace(c))
				{
					// Write single space character
					int len = _currLine.length();
					if (len == 0 || !Character.isWhitespace(_currLine.charAt(len - 1)))
						_currLine.append(' ');
				}
				else
				{
					// Add character to current line
					_currLine.append(c);

			//		System.out.print("**"+c+"@@");
				}
			}
		//	System.out.print("((!!!"+_text.length()+"!!!)))");
		}

		// Appends the current line to output buffer
		protected void FlushCurrLine()
		{
			// Get current line
			String line = _currLine.toString().trim();
			// Determine if line contains non-space characters
			String tmp = line.replace("&nbsp;","");
			if (tmp.length() == 0)
			{
				// An empty line
				_emptyLines++;
				if (_emptyLines < 2 && _text.length() > 0)
					_text.append(LINE_SEPARATOR+line);
			}
			else
			{
				// A non-empty line
				_emptyLines = 0;
				_text.append(LINE_SEPARATOR+line);
			}

			// Reset current line
			_currLine.setLength(0);
		}

		// Returns the current output as a string.
		public String toString()
		{
			if (_currLine.length() > 0)
				FlushCurrLine();
			return _text.toString();
		}
	}
	
	
	public int indexOfAny(String str,char[] chars,int position){
	   int index=-1;
		for(char c:chars){
	    	index=str.indexOf(c, position);
	    	if(index!=-1){
	    		System.out.print(index+"-");
	    		return index;
	    	}
	    }
	//	System.out.println(index+"^^");
		return index;
	}
	
	



}






